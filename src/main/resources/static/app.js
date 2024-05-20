var autoRefreshIntervalId = null;

function refreshTimeTable() {
  $.getJSON("/timeTable", function (timeTable) {
    refreshSolvingButtons(timeTable.solverStatus != null && timeTable.solverStatus !== "NOT_SOLVING");
    $("#score").text("Score: " + (timeTable.score == null ? "?" : timeTable.score));

    const timeTableByRoom = $("#timeTableByRoom");
    timeTableByRoom.children().remove();
    const timeTableByTeacher = $("#timeTableByTeacher");
    timeTableByTeacher.children().remove();
    const timeTableByStudentGroup = $("#timeTableByStudentGroup");
    timeTableByStudentGroup.children().remove();
    const unassignedLessons = $("#unassignedLessons");
    unassignedLessons.children().remove();

    const theadByRoom = $("<thead>").appendTo(timeTableByRoom);
    const headerRowByRoom = $("<tr>").appendTo(theadByRoom);
    headerRowByRoom.append($("<th> </th>"));
    $.each(timeTable.roomList, (index, room) => {
      headerRowByRoom
          .append($(`<th>`)
              .append($("<span style='white-space: nowrap;'/>").text(`Номер: ${room.code}`))
              .append($("<br/>"))
              .append($("<span style='white-space: nowrap;'/>").text(room.type))
              .append($("<br/>"))
              .append($("<span style='white-space: nowrap;'/>").text(`Размер: ${room.capacity}`)));
    });
    const theadByTeacher = $("<thead>").appendTo(timeTableByTeacher);
    const headerRowByTeacher = $("<tr>").appendTo(theadByTeacher);
    headerRowByTeacher.append($("<th>Timeslot</th>"));
    const teacherList = timeTable.teacherList;
    $.each(teacherList, (index, teacher) => {
      headerRowByTeacher
        .append($(`<th style="text-align: center;">`)
          .append($("<span style='white-space: nowrap;'/>").text(teacher.code)));
    });
    const theadByStudentGroup = $("<thead>").appendTo(timeTableByStudentGroup);
    const headerRowByStudentGroup = $("<tr>").appendTo(theadByStudentGroup);
    headerRowByStudentGroup.append($("<th>Timeslot</th>"));
    const studentGroupList = timeTable.curriculumList;
    $.each(studentGroupList, (index, studentGroup) => {
      headerRowByStudentGroup
        .append($(`<th style="text-align: center;">`)
          .append($("<span/>").text(studentGroup.code)));
    });

    const tbodyByRoom = $("<tbody>").appendTo(timeTableByRoom);
    const tbodyByTeacher = $("<tbody>").appendTo(timeTableByTeacher);
    const tbodyByStudentGroup = $("<tbody>").appendTo(timeTableByStudentGroup);

    $.each(timeTable.periodList, (index, period) => {
      const rowByRoom = $("<tr>").appendTo(tbodyByRoom);
      const firstSpaceIndex = period.label.search(/\s/);
      const day = period.label.substring(0, firstSpaceIndex);
      const time = period.label.substring(firstSpaceIndex + 1);

      rowByRoom
          .append($(`<th class="align-middle"/>`)
              .append($("<span/>").text(day))
              .append($("<br/>"))
              .append($("<span/>").text(time)));
      $.each(timeTable.roomList, (index, room) => {
        rowByRoom.append($("<td/>").prop("id", `timeslot${period.id}room${room.id}`));
      });

      const rowByTeacher = $("<tr>").appendTo(tbodyByTeacher);
      rowByTeacher
          .append($(`<th class="align-middle"/>`)
              .append($("<span/>").text(day))
              .append($("<br/>"))
              .append($("<span/>").text(time)));
      $.each(teacherList, (index, teacher) => {
        rowByTeacher.append($("<td/>").prop("id", `timeslot${period.id}teacher${teacher.id}`));
      });
      const rowByStudentGroup = $("<tr>").appendTo(tbodyByStudentGroup);
      rowByStudentGroup
          .append($(`<th class="align-middle"/>`)
              .append($("<span/>").text(day))
              .append($("<br/>"))
              .append($("<span/>").text(time)));
      $.each(studentGroupList, (index, studentGroup) => {
        rowByStudentGroup.append($("<td/>").prop("id", `timeslot${period.id}studentGroup${studentGroup.id}`));
      });
    });

    $.each(timeTable.lectureList, (index, lecture) => {
      const color = pickColor(lecture.course.code);
      const studentGroups = Array.from(lecture.course.curriculumSet).map(curriculum => curriculum.code).join(", ");
      const lessonElementWithoutDelete = $(`<div class="card" style="background-color: ${color}"/>`)
          .append($(`<div class="card-body p-2"/>`)
              .append($(`<h5 class="card-title mb-1"/>`).text(lecture.course.code))
              .append($(`<p class="card-text ml-2 mb-1"/>`)
                  .append($(`<em/>`).text(`${lecture.course.teacher.code}`)))
              .append($(`<p class="card-text ml-2 mb-0"/>`).text(studentGroups))
              .append($(`<p class="card-text ml-2"/>`).text(`Количество студентов: ${lecture.course.studentSize}`)));
      const lessonElement = lessonElementWithoutDelete.clone();
      lessonElement.find(".card-body").prepend(
        $(`<button type="button" class="ml-2 btn btn-light btn-sm p-1 float-right"/>`)
          .append($(`<small class="fas fa-trash"/>`)
          ).click(() => deleteLesson(lecture))
      );
      if (lecture.period == null || lecture.room == null) {
        unassignedLessons.append(lessonElement);
      } else {
        $(`#timeslot${lecture.period.id}room${lecture.room.id}`).append(lessonElement);
        $(`#timeslot${lecture.period.id}teacher${lecture.course.teacher.id}`).append(lessonElementWithoutDelete.clone());
        lecture.course.curriculumSet.forEach(curriculum => {
          $(`#timeslot${lecture.period.id}studentGroup${curriculum.id}`).append(lessonElementWithoutDelete.clone());
        });
      }
    });
  });
}

function solve() {
  $.post("/timeTable/solve", function () {
    refreshSolvingButtons(true);
  }).fail(function (xhr, ajaxOptions, thrownError) {
    showError("Start solving failed.", xhr);
  });
}

function refreshSolvingButtons(solving) {
  if (solving) {
    $("#solveButton").hide();
    $("#stopSolvingButton").show();
    if (autoRefreshIntervalId == null) {
      autoRefreshIntervalId = setInterval(refreshTimeTable, 2000);
    }
  } else {
    $("#solveButton").show();
    $("#stopSolvingButton").hide();
    if (autoRefreshIntervalId != null) {
      clearInterval(autoRefreshIntervalId);
      autoRefreshIntervalId = null;
    }
  }
}

function stopSolving() {
  $.post("/timeTable/stopSolving", function () {
    refreshSolvingButtons(false);
    refreshTimeTable();
  }).fail(function (xhr, ajaxOptions, thrownError) {
    showError("Stop solving failed.", xhr);
  });
}

function addLesson() {
  var subject = $("#lesson_subject").val().trim();
  $.post("/lessons", JSON.stringify({
    "subject": subject,
    "teacher": $("#lesson_teacher").val().trim(),
    "studentGroup": $("#lesson_studentGroup").val().trim()
  }), function () {
    refreshTimeTable();
  }).fail(function (xhr, ajaxOptions, thrownError) {
    showError("Adding lesson (" + subject + ") failed.", xhr);
  });
  $('#lessonDialog').modal('toggle');
}

function deleteLesson(lesson) {
  $.delete("/lessons/" + lesson.id, function () {
    refreshTimeTable();
  }).fail(function (xhr, ajaxOptions, thrownError) {
    showError("Deleting lesson (" + lesson.name + ") failed.", xhr);
  });
}

function addTimeslot() {
  $.post("/timeslots", JSON.stringify({
    "dayOfWeek": $("#timeslot_dayOfWeek").val().trim().toUpperCase(),
    "startTime": $("#timeslot_startTime").val().trim(),
    "endTime": $("#timeslot_endTime").val().trim()
  }), function () {
    refreshTimeTable();
  }).fail(function (xhr, ajaxOptions, thrownError) {
    showError("Adding timeslot failed.", xhr);
  });
  $('#timeslotDialog').modal('toggle');
}

function deleteTimeslot(timeslot) {
  $.delete("/timeslots/" + timeslot.id, function () {
    refreshTimeTable();
  }).fail(function (xhr, ajaxOptions, thrownError) {
    showError("Deleting timeslot (" + timeslot.name + ") failed.", xhr);
  });
}

function addRoom() {
  var name = $("#room_name").val().trim();
  $.post("/rooms", JSON.stringify({
    "name": name
  }), function () {
    refreshTimeTable();
  }).fail(function (xhr, ajaxOptions, thrownError) {
    showError("Adding room (" + name + ") failed.", xhr);
  });
  $("#roomDialog").modal('toggle');
}

function deleteRoom(room) {
  $.delete("/rooms/" + room.id, function () {
    refreshTimeTable();
  }).fail(function (xhr, ajaxOptions, thrownError) {
    showError("Deleting room (" + room.name + ") failed.", xhr);
  });
}

function showError(title, xhr) {
  const serverErrorMessage = !xhr.responseJSON ? `${xhr.status}: ${xhr.statusText}` : xhr.responseJSON.message;
  console.error(title + "\n" + serverErrorMessage);
  const notification = $(`<div class="toast" role="alert" aria-live="assertive" aria-atomic="true" style="min-width: 30rem"/>`)
    .append($(`<div class="toast-header bg-danger">
                 <strong class="mr-auto text-dark">Error</strong>
                 <button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">
                   <span aria-hidden="true">&times;</span>
                 </button>
               </div>`))
    .append($(`<div class="toast-body"/>`)
      .append($(`<p/>`).text(title))
      .append($(`<pre/>`)
        .append($(`<code/>`).text(serverErrorMessage))
      )
    );
  $("#notificationPanel").append(notification);
  notification.toast({delay: 30000});
  notification.toast('show');
}

$(document).ready(function () {
  $.ajaxSetup({
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    }
  });
  // Extend jQuery to support $.put() and $.delete()
  jQuery.each(["put", "delete"], function (i, method) {
    jQuery[method] = function (url, data, callback, type) {
      if (jQuery.isFunction(data)) {
        type = type || callback;
        callback = data;
        data = undefined;
      }
      return jQuery.ajax({
        url: url,
        type: method,
        dataType: type,
        data: data,
        success: callback
      });
    };
  });

  $("#refreshButton").click(function () {
    refreshTimeTable();
  });
  $("#solveButton").click(function () {
    solve();
  });
  $("#stopSolvingButton").click(function () {
    stopSolving();
  });
  $("#addLessonSubmitButton").click(function () {
    addLesson();
  });
  $("#addTimeslotSubmitButton").click(function () {
    addTimeslot();
  });
  $("#addRoomSubmitButton").click(function () {
    addRoom();
  });

  refreshTimeTable();
});

// ****************************************************************************
// TangoColorFactory
// ****************************************************************************

const SEQUENCE_1 = [0x8AE234, 0xFCE94F, 0x729FCF, 0xE9B96E, 0xAD7FA8];
const SEQUENCE_2 = [0x73D216, 0xEDD400, 0x3465A4, 0xC17D11, 0x75507B];

var colorMap = new Map;
var nextColorCount = 0;

function pickColor(object) {
  let color = colorMap[object];
  if (color !== undefined) {
    return color;
  }
  color = nextColor();
  colorMap[object] = color;
  return color;
}

function nextColor() {
  let color;
  let colorIndex = nextColorCount % SEQUENCE_1.length;
  let shadeIndex = Math.floor(nextColorCount / SEQUENCE_1.length);
  if (shadeIndex === 0) {
    color = SEQUENCE_1[colorIndex];
  } else if (shadeIndex === 1) {
    color = SEQUENCE_2[colorIndex];
  } else {
    shadeIndex -= 3;
    let floorColor = SEQUENCE_2[colorIndex];
    let ceilColor = SEQUENCE_1[colorIndex];
    let base = Math.floor((shadeIndex / 2) + 1);
    let divisor = 2;
    while (base >= divisor) {
      divisor *= 2;
    }
    base = (base * 2) - divisor + 1;
    let shadePercentage = base / divisor;
    color = buildPercentageColor(floorColor, ceilColor, shadePercentage);
  }
  nextColorCount++;
  return "#" + color.toString(16);
}

function buildPercentageColor(floorColor, ceilColor, shadePercentage) {
  let red = (floorColor & 0xFF0000) + Math.floor(shadePercentage * ((ceilColor & 0xFF0000) - (floorColor & 0xFF0000))) & 0xFF0000;
  let green = (floorColor & 0x00FF00) + Math.floor(shadePercentage * ((ceilColor & 0x00FF00) - (floorColor & 0x00FF00))) & 0x00FF00;
  let blue = (floorColor & 0x0000FF) + Math.floor(shadePercentage * ((ceilColor & 0x0000FF) - (floorColor & 0x0000FF))) & 0x0000FF;
  return red | green | blue;
}
