document.addEventListener("DOMContentLoaded", function () {


  const today = new Date();
  const nextDay = new Date(today);
  nextDay.setDate(today.getDate() + 1);
  const dateString = nextDay.toISOString().split("T")[0];

  document.getElementById("fromDate").setAttribute("min", dateString);
  document.getElementById("toDate").setAttribute("min", dateString);

  displayAccounInfo();

});

document.getElementById("account-info").addEventListener('click', displayAccounInfo);

const closeButton = document.querySelector('#dialog .close');
if (closeButton) {
  closeButton.addEventListener('submit', closeDialog);
}


function fetchLeaveRequests() {

  const leaveRequestsUrl = "http://localhost:8080/api/leave_management/my_team_leave_requests";

  hideContainer();

  fetch(leaveRequestsUrl, {
    method: 'GET'
  })
    .then(response => response.json())
    .then(data => {

      document.getElementById("content-title").textContent = "Team Leave Requests";

      const tableBody = document.querySelector('#leaves-list tbody');

      tableBody.innerHTML = '';
      // console.log(data);

      if (data.length === 0) {
        tableBody.appendChild(displayNoData("Your are not a manager."));
      }
      else {
        const tableHeader = document.createElement('tr');
        tableHeader.innerHTML = `
                  <th>Employee Name</th>
                 <!-- <th id="leave-id">Leave ID</th> -->
                  <th>Leave Type</th>
                  <th>Applied on</th>
                  <th>From Date</th>
                  <th>To Date</th>
                  <th>Leave Description</th>
                  <th id="statusOfLeave">Status</th>
                  <th>Action</th>
                  `;

        tableBody.appendChild(tableHeader);

        data.forEach(request => {

          const daysRequested = calculateWeekdaysBetween(request.fromDate, request.toDate);

          const row = document.createElement('tr');
          row.innerHTML = `
                      <td>${request.employeeName}</td>
                     <!-- <td>${request.leaveId}</td> -->
                      <td>${request.leaveType}</td>
                      <td>${request.appliedOn}</td>
                      <td>${request.fromDate}</td>
                      <td>${request.toDate}</td>
                      <td>${request.leaveDescription}</td>
                      <td >${request.statusOfLeave}</td>
                      <td>
                          <button class="btn btn-info btn-sm mb-2 action-btn" onClick="askForConfirmation('${request.leaveId}','${request.employeeId}','${request.leaveType}', '${daysRequested}')" >Action on leave</button>
                      </td>
                  `;

          tableBody.appendChild(row);

          const actionButton = row.querySelector(".action-btn");

          if (request.statusOfLeave !== "Pending") {
            actionButton.style.display = 'none';
          }

        });
      }
    })
    .catch(error => console.error('Error fetching leave requests:', error));
}


function calculateWeekdaysBetween(fromDateStr, toDateStr) {
  const fromDate = new Date(fromDateStr);
  const toDate = new Date(toDateStr);


  if (fromDate > toDate) {
    throw new Error("fromDate should be before or equal to toDate.");
  }

  let weekdayCount = 0;
  let currentDate = new Date(fromDate);

  while (currentDate <= toDate) {
    const dayOfWeek = currentDate.getDay();
    if (dayOfWeek !== 0 && dayOfWeek !== 6) {
      weekdayCount++;
    }
    currentDate.setDate(currentDate.getDate() + 1);
  }

  return weekdayCount;
}


async function askForConfirmation(leaveId, employeeId, leaveType, daysRequested) {

  console.log(employeeId, leaveType);

  const data = {
    employeeId,
    leaveType
  }

  console.log(data);


  try {

    const queryString = new URLSearchParams(data).toString();

    const response = await fetch(`http://localhost:8080/api/leave_management/leaveConfirmation?${queryString}`, {
      method: 'GET'
    });

    if (!response.ok) {
      throw new Error(`Error fetching employee details: ${response.statusText}`);
    }

    const employeeLeave = await response.json();
    console.log(employeeLeave);

    const dialog = document.getElementById('dialog');
    const detailsDiv = document.getElementById('employeeDetails');
    detailsDiv.innerHTML = `
        <p>Leave Type: ${employeeLeave.leaveType}</p>
        <p>Days Taken: ${employeeLeave.leavesTaken}</p>
        <p>Allowed Days: ${employeeLeave.allowedDays}</p>
        <p>Days requesting : ${daysRequested}</p> 
    `;
    dialog.style.display = 'block';

    const closeDialog = document.getElementById('closeAction');

    closeButton.addEventListener('click',()=>{
      dialog.style.display = 'none';
    })

    document.getElementById('approveButton').addEventListener('click', () => {
      updateStatus(leaveId, 'Approved');
      dialog.style.display = 'none';
    });


    document.getElementById('rejectButton').addEventListener('click', () => {
      updateStatus(leaveId, 'Rejected');
      dialog.style.display = 'none';
    });

  } catch (error) {
    console.error('Failed to fetch employee details:', error);
    alert('An error occurred while fetching employee details. Please try again later.');
  }

}


// function validationMessage(message){

//   const dialog = document.getElementById('validation-dialog');
//     const detailsDiv = document.getElementById('errorMessage');
//     detailsDiv.innerHTML = `
//         <p>Error: ${message}</p>
//     `;
//     dialog.style.display = 'block';

//     const closeDialog = document.getElementById('closevalidationDialog');

//     closeDialog.addEventListener('click',()=>{
//       dialog.style.display = 'none';
//     })
// }




async function updateStatus(leaveId, status) {
  console.log(leaveId, status);

  try {

    const response = await fetch(`http://localhost:8080/api/leave_management/my_leaves_tracker`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ leaveId, status })
    });

    if (!response.ok) {
      throw new Error(`Error updating status: ${response.statusText}`);
    }
    console.log(response);

    console.log(`Employee status updated to ${status}.`);
    fetchLeaveRequests();

  } catch (error) {
    console.error('Failed to update employee status:', error);
    alert('An error occurred while updating the status. Please try again later.');
  }
}



async function fetchMyLeaves() {
  const myLeavesUrl = 'http://localhost:8080/api/leave_management/my_leaves_tracker';


  hideContainer();

  try {
    const response = await fetch(myLeavesUrl, { method: 'GET' });

    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }

    const data = await response.json();
    console.log(data);


    document.getElementById("content-title").textContent = "My Leave Requests";

    const tableBody = document.querySelector('#leaves-list tbody');
    tableBody.innerHTML = '';

    if (data.length === 0) {
      tableBody.appendChild(displayNoData("No leave requests found"))
      // tableBody.innerHTML = '<tr><td colspan="5">No leave requests found</td></tr>';
      return;
    }

    const tableHeader = document.createElement('tr');
    tableHeader.innerHTML = `
      <th>Leave Type</th>
      <th>Applied on</th>
       <th>From Date</th>
      <th>To Date</th>
      <th>Leave Description</th>
      <th>Status</th>
    `;
    tableBody.appendChild(tableHeader);


    data.forEach(leave => {
      const row = document.createElement('tr');
      row.innerHTML = `
        <td>${escapeHtml(leave.leaveType)}</td>
        <td>${escapeHtml(leave.appliedOn)}</td>
        <td>${escapeHtml(leave.fromDate)}</td>
        <td>${escapeHtml(leave.toDate)}</td>
        <td>${escapeHtml(leave.leaveDescription)}</td>
        <td>${getLeaveStatusIcon(leave.statusOfLeave)}</td>
      `;
      tableBody.appendChild(row);
    });
  } catch (error) {
    console.error('Error fetching my leaves:', error);
  }
}


function escapeHtml(text) {
  const map = {
    '&': '&amp;',
    '<': '&lt;',
    '>': '&gt;',
    '"': '&quot;',
    "'": '&#039;'
  };
  return text.replace(/[&<>"']/g, (m) => map[m]);
}


function getLeaveStatusIcon(status) {
  switch (status) {
    case 'Rejected':
      return '❌';
    case 'Approved':
      return '✅';
    case 'Pending':
      return '⏳';
  }
}


function fetchUpcomingHolidays() {
  fetch("http://localhost:8080/api/leave_management/holidays", {
    method: "GET",
    headers: {
      "Content-Type": "application/json"
    }
  })
    .then(response => {
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      return response.json();
    })
    .then(data => {

      console.log("Data:", data);

      const holidaysContainer = document.getElementById('holidaysContainer');

      if (!holidaysContainer) {
        console.error('Element with ID "holidaysContainer" not found.');
        return;
      }

      holidaysContainer.innerHTML = '';

      data.forEach(holiday => {
        const holidayElement = document.createElement('div');
        holidayElement.className = 'holiday mt-2';

        holidayElement.innerHTML = `
      <div class="holiday-card">
          <div class="d-flex justify-content-between">
              <div class="holiday-name">
                  <b>${holiday.holidayName}</b>
              </div>
              <div class="holiday-details">
                  <div class="holiday-day">${holiday.day}</div>
                  <div>${holiday.holidayDate}</div>
              </div>
          </div>
      </div>
  `;

        holidaysContainer.appendChild(holidayElement);
      });
    })
    .catch(error => {
      console.error('Error:', error);
    });
}


var gender = "";
var manager_name;
var employee_name;

function displayAccounInfo() {

  const accountInfoUrl = "http://localhost:8080/api/leave_management/my_account";

  fetch(accountInfoUrl, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    }
  })
    .then(response => {
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      return response.json();
    })
    .then(response => {

      // console.log(response);


      gender = response.gender;
      // console.log("gender in acc : ", gender);

      updateLeaveOptions(gender);

      document.getElementById("userName").textContent = `${response.employeeName}`;

      document.getElementById('employeeName').innerHTML = `<b>Name: ${response.employeeName} </b>`;
      document.getElementById('employeeId').innerHTML = `<b>Employee Id: ${response.employeeId} </b>`;
      document.getElementById('myMobileNumber').innerHTML = `<b>Mobile Number: ${response.mobileNumber} </b>`;
      document.getElementById('myEmailId').innerHTML = `<b>Email Id: ${response.emailId} </b>`;

      employee_name = response.employeeName;
      manager_name = response.managerName;

      if (employee_name === manager_name) {
        var button = document.querySelector(".apply-leave-btn");
        if (button) {
          button.style.display = 'none';
        }
      }

      if (response.employeeName !== response.managerName) {
        document.getElementById('myMangerName').innerHTML = `<b>Reports to: ${response.managerName} </b>`;
      }

    })
    .catch(error => {
      console.error('Error:', error);
    });
}




const leaveTypeSelect = document.getElementById('leaveType');

function updateLeaveOptions(gender) {
  const paternityOption = leaveTypeSelect.querySelector('option[value="Paternity Leave"]');
  const maternityOption = leaveTypeSelect.querySelector('option[value="Maternity Leave"]');

  if (gender === 'Male') {
    paternityOption.style.display = '';
    maternityOption.style.display = 'none';
  } else if (gender === 'Female') {
    paternityOption.style.display = 'none';
    maternityOption.style.display = '';
  } else {
    paternityOption.style.display = '';
    maternityOption.style.display = '';
  }
}



function applyLeave() {

  console.log("gender in apply leave", gender);
  const leaveType = document.getElementById("leaveType").value;
  const fromDate = document.getElementById("fromDate").value;
  const toDate = document.getElementById("toDate").value;
  const leaveDescription = document.getElementById("leaveDescription").value;

  if (!leaveType) {

    alert("Leave type cannot be empty");
    return;
  }

  if (!fromDate || !toDate) {
    alert("Date cannot be empty");
    return;
  }

  const now = new Date();
  const currentDate = now.toISOString().split("T")[0];


  if (toDate < fromDate) {
    alert("To date should be after from date");
    return;
  }

  document.querySelector('#requestsModal').click();

  const leave = {
    leaveType,
    fromDate,
    toDate,
    leaveDescription,
    appliedOn: currentDate,
    statusOfLeave: "Pending"
  };

  console.log(JSON.stringify(leave));

  fetch("http://localhost:8080/api/leave_management/my_leaves_tracker", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(leave),
  })
    .then(async (response) => {
      if (!response.ok) {
        const text = await response.text();
        throw new Error(`Network response was not ok: ${text}`);
      }
      return response.json();
    })
    .then((data) => {
      console.log("Success:", data);
    })
    .catch((error) => {
      console.error("Error:", error);
      alert("There was an error processing your request. " + error.message);
    });

  document.getElementById("leaveType").value = "";
  document.getElementById("fromDate").value = "";
  document.getElementById("toDate").value = "";
  document.getElementById("leaveDescription").value = "";


}


function fetchDashboard() {
  hideContainer();

  console.log("Gender : ", gender);

  const dashboardUrl = "http://localhost:8080/api/leave_management/dashboard";

   fetch(dashboardUrl, { method: "GET" })
    .then(response => {
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      return response.json();
    })
    .then(data => {

      if (data.length === 0) {
        displayNoData("No leaves taken");
        return;
      }

      console.log(data);

      const leaveTypes = data;

      const leaveTypeColorMap = {
        'Annual Leave': 'bg-success',
        'Sick Leave': 'bg-warning',
        'Casual Leave': 'bg-info',
        'Maternity Leave': 'bg-primary',
        'Paternity Leave': 'bg-primary',
        'Emergency Leave': 'bg-secondary',
      };


      leaveTypes.forEach(leave => {
        leave.progress = Math.min((leave.daysTaken / leave.allowedDays) * 100, 100);
        leave.color = leaveTypeColorMap[leave.leaveType];
        // console.log(leave.color,leaveTypeColorMap[leave.type]);
        
      })

      console.log(leaveTypes);


      document.getElementById("content-title").textContent = "My Leave Tracker";

      const tableBody = document.querySelector('#leaves-list tbody');
      tableBody.innerHTML = '';

      const leaveSummary = document.createElement('tr');
      let space = " ";
      leaveSummary.innerHTML = `
      <div class="row">
          ${leaveTypes.map(leave => `
              <div class="col-md-6 col-lg-4">
                  <div class="card leave-card mb-5 mt-2">
                      <div class="card-header">
                          <b>${leave.leaveType}</b>
                      </div>
                      <div class="card-body">
                          <div class="progress">
                              
                              <div class="progress-bar w-100 bg-light text-dark">
                                   <div
                                  class="progress-bar bg-success"
                                  role="progressbar"
                                  style="width: ${leave.progress}%"
                                  aria-valuenow="${leave.progress}"
                                  aria-valuemin="0"
                                  aria-valuemax="100"
                              >
                                   
                              </div>
                                   ${leave.daysTaken}/${leave.allowedDays} Days Taken
                              </div>
                             
                              
                          </div>
                      </div>
                  </div>
              </div>
          `).join('')
        }
      </div>
        `;
        // console.log(leaveSummary);
        
      tableBody.appendChild(leaveSummary);
    })

    .catch(error => {
      console.error('Error fetching data:', error);
    });
}


function logout() {
  const logoutUrl = "http://localhost:8080/api/leave_management/logout";

  fetch(logoutUrl, {
    method: 'GET',
  })
    .then(response => {
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      return response.json();
    })
    .then(data => {
      console.log('Response data:', data);

      if (data.status === 'success') {
        window.location.href = 'http://localhost:8080/api/leave_management/index.html';
      } else {
        alert('Error logging out: ' + data.message);
      }
    })
    .catch(error => {
      console.error('Logout error:', error);
      alert('An error occurred during logout. Please try again.');
    });
}


// to hide welcome 
function hideContainer() {
  const welcomeContainer = document.querySelector(".welcome-container");
  if (welcomeContainer) {
    welcomeContainer.style.display = "none";
  }
}


function closeDialog() {
  const dialog = document.getElementById('dialog');
  if (dialog) {
    dialog.style.display = 'none';
  }
}

// to display when no data available
function displayNoData(message) {
  const noDataDiv = document.createElement('div');
  noDataDiv.className = 'no-data';
  noDataDiv.innerHTML = `
      <h2>${message}</h2>
      <img id="noDataImage" src="assets/images/noDataFoundImage.png" alt="No Data Found" />
  `;
  return noDataDiv;
}
