document.addEventListener("DOMContentLoaded", function () {

  // document.getElementById("content-title").textContent = "";
  const today = new Date();
  const dateString = today.toISOString().split("T")[0];
  document.getElementById("fromDate").setAttribute("min", dateString);
  document.getElementById("toDate").setAttribute("min", dateString);

  document.getElementById("account-info").addEventListener('click', displayAccounInfo());

  const gender = "";
  console.log(gender,"on load");
  
  const leaveTypeSelect = document.getElementById('leaveType');

            // Function to update leave options based on gender
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

  // const dashboardButton = document.getElementById("dashboardButton ");
  // const myleavesButton = document.getElementById("myleavesButton");
  // const myLeaveRequestsButton = document.getElementById("myLeaveRequestsButton");
  // const holidaysButton = document.getElementById("holidaysButton");

  // const welcomeContainer = document.querySelector(".welcome-container");

  // // Function to hide the container
  // function hideContainer() {
  //     if (welcomeContainer) {
  //         welcomeContainer.style.display = "none";
  //     }
  //     fetchDashboard();
  // }

  // // Add click event listener to the button
  // if (dashboardButton) {
  //   dashboardButton.addEventListener("click", hideContainer);

  // }



  // document.getElementById("logout-btn").addEventListener()

  // document.getElementById("toDate").setAttribute("max",dateString+4);


  // function openModal() {
  //   $('#exampleModalCenter').modal('show');
  // }



  // const approveButtons = document.getElementsByClassName('approve-btn').addEventListener('click', handleButtonClick('approve'));
  // const rejectButtons = document.getElementsByClassName('reject-btn').addEventListener('click', handleButtonClick('reject'));

  // const modalText = document.getElementById('modal-text');
  // const confirmButton = document.getElementById('confirm-btn');

  // const showModal = document.getElementById('#exampleModalCenter')


  // Function to handle button clicks
  // function handleButtonClick(action) {
  //   return function () {
  //     if (action === 'approve') {
  //       modalText.textContent = 'Confirm Approve';
  //     } else if (action === 'reject') {
  //       modalText.textContent = 'Confirm Reject';
  //     }
  //     document.getElementById('#exampleModalCenter').modal('show'); // Show modal
  //   }
  // }

});



const leaveRequestsUrl = "http://localhost:8080/api/leave_management/my_team_leave_requests"; // API for leave requests
// API for my leaves

function fetchLeaveRequests() {


  hideContainer();

  // document.getElementsByClassName('content')[0].innerHTML = '';

  fetch(leaveRequestsUrl, {
    method: 'GET'
  })
    .then(response => response.json())
    .then(data => {

      document.getElementById("content-title").textContent = "Team Leave Requests";

      const tableBody = document.querySelector('#leaves-list tbody');

      tableBody.innerHTML = ''; // Clear existing rows

      // console.log(data);
      
      if (data.length === 0) {
        tableBody.appendChild(displayNoData("Your are not a manager."));
      }
      else {
        // document.getElementById("noDataImage").style.display = 'none !important';
        const tableHeader = document.createElement('tr');
        tableHeader.innerHTML = `
                  <th>Employee Name</th>
                  <!-- <th id="leave-id">Leave ID</th> -->
                  <th>Leave Type</th>
                  <th>From Date</th>
                  <th>To Date</th>
                  <th>Leave Description</th>
                  <th>Status</th>
                  <th>Action</th>
                  `;

        tableBody.appendChild(tableHeader);

        data.forEach(request => {

          const row = document.createElement('tr');
          row.innerHTML = `
                      <td>${request.employeeName}</td>
                      <!-- <td>${request.leaveId}</td> -->
                      <td>${request.leaveType}</td>
                      <td>${request.fromDate}</td>
                      <td>${request.toDate}</td>
                      <td>${request.leaveDescription}</td>
                      <td>${request.statusOfLeave}</td>
                      <td>
                          <button class="btn btn-success btn-sm mb-2 approve-btn">Approve</button>
                          <button class="btn btn-danger btn-sm reject-btn">Reject</button>
                      </td>
                  `;

          tableBody.appendChild(row);
          // Get the buttons for the current row
          const approveButton = row.querySelector('.approve-btn');
          const rejectButton = row.querySelector('.reject-btn');
          // const leaveId = row.querySelector("#leave-id");



          // leaveId.style.display = 'none';

          // Hide buttons if status is not "Pending"
          if (request.statusOfLeave !== "Pending") {
            approveButton.style.display = 'none';
            rejectButton.style.display = 'none';
          }

        });
      }
    })
    .catch(error => console.error('Error fetching leave requests:', error));
}

const approveButton = document.getElementsByClassName("approve-btn");
const rejectButton = document.getElementsByClassName("reject-btn");




const myLeavesUrl = 'http://localhost:8080/api/leave_management/my_leaves_tracker';

async function fetchMyLeaves() {


  hideContainer();

  // const contentElement = document.getElementsByClassName('content')[0];
  // console.log(contentElement);

  // contentElement.innerHTML = '';

  try {
    const response = await fetch(myLeavesUrl, { method: 'GET' });

    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }

    const data = await response.json();
    console.log("data", data);

    
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
      <th>Request Date</th>
      <th>To Date</th>
      <th>Leave Description</th>
      <th>Status</th>
    `;
    tableBody.appendChild(tableHeader);


    data.forEach(leave => {
      const row = document.createElement('tr');
      row.innerHTML = `
        <td>${escapeHtml(leave.leaveType)}</td>
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

// Helper function to escape HTML characters
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


document.getElementById("applyLeave").addEventListener("click", function (event) {
  event.preventDefault(); // Prevent the default form submission

  applyLeave(); // Call the login function
});



function applyLeave() {
  const leaveType = document.getElementById("leaveType").value;
  const fromDate = document.getElementById("fromDate").value;
  const toDate = document.getElementById("toDate").value;
  const leaveDescription = document.getElementById("leaveDescription").value;

  if (!leaveType) {
    // alert("Leave type cannot be empty");
    return;
  }

  if (!fromDate || !toDate) {
    // alert("Date cannot be empty");
    return;
  }

  const now = new Date();
  const currentDate = now.toISOString().split("T")[0];

  if (toDate < currentDate) {
    // alert("To date should be in the future, not in the past.");
    return;
  }

  console.log("gender in apply leave",gender);
  const leave = {
    leaveType,
    fromDate,
    toDate,
    leaveDescription,
    appliedOn: currentDate,
    statusOfLeave: "Pending"
  };



  console.log(JSON.stringify(leave));



  fetch("api/leave_management/my_leaves_tracker", {
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
      // loadLeaves();
    })
    .catch((error) => {
      console.error("Error:", error);
      // alert("There was an error processing your request. " + error.message);
    });

  // Clear form fields
  document.getElementById("leaveType").value = "";
  document.getElementById("fromDate").value = "";
  document.getElementById("toDate").value = "";
  document.getElementById("leaveDescription").value = "";

}


function fetchDashboard() {
  hideContainer();

  const dashboardUrl = "http://localhost:8080/api/leave_management/dashboard";
  fetch( dashboardUrl, {
    method: "GET",
    headers: {
      "Content-Type": "application/json" // Headers should be an object
    }
  })
  .then(response => {
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`); // Use backticks for template literals
    }
    return response.json();
  })
  .then(data => {
    console.log(data);
  })


  .catch(error => {
    console.log(error);
  })

  document.getElementById("content-title").textContent = "My Leave Tracker";

  const tableBody = document.querySelector('#leaves-list tbody');
  tableBody.innerHTML = '';


  // leavesData.innerHTML = ``;

  // Create a container for the leave summary
  const leaveSummary = document.createElement('tr');
  // leaveSummary.className = 'container'; // Add a class for styling
  // console.log("Created..!!");

  // Define the leave types data
  const leaveTypes = [
    { type: 'Annual Leave', color: 'bg-success', progress: 60, total: '20', taken: '12' },
    { type: 'Sick Leave', color: 'bg-warning', progress: 30, total: '10', taken: '3' },
    { type: 'Casual Leave', color: 'bg-info', progress: 40, total: '15', taken: '7' },
    { type: 'Maternity Leave', color: 'bg-danger', progress: 70, total: '90', taken: '63' },
    { type: 'Paternity Leave', color: 'bg-primary', progress: 50, total: '10', taken: '5' },
    { type: 'Emergency Leave', color: 'bg-secondary', progress: 40, total: '5', taken: '2' }
  ];


  // Build the HTML content for leave summary
  // <h2 class="my-4">Leave Tracker</h2> 
  leaveSummary.innerHTML = `
      <div class="row">
          ${leaveTypes.map(leave => `
              <div class="col-md-6 col-lg-4">
                  <div class="card leave-card">
                      <div class="card-header">
                          <b>${leave.type}</b>
                      </div>
                      <div class="card-body">
                          <div class="progress">
                              <div class="progress-bar bg-light" style="width: 100%"></div>
                              <div
                                  class="progress-bar ${leave.color}"
                                  role="progressbar"
                                  style="width: ${leave.progress}%"
                                  aria-valuenow="${leave.progress}"
                                  aria-valuemin="0"
                                  aria-valuemax="100"
                              >
                                  <span class="progress-text">${leave.taken}/${leave.total} Days Taken</span>
                              </div>
                          </div>
                      </div>
                  </div>
              </div>
          `).join('')}
      </div>
  `;

  // document.getElementsByClassName('content')[0].innerHTML = '';

  // Append the leave summary to the content element
  tableBody.appendChild(leaveSummary);
}


function fetchUpcomingHolidays() {
  fetch("http://localhost:8080/api/leave_management/holidays", {
    method: "GET",
    headers: {
      "Content-Type": "application/json" // Headers should be an object
    }
  })
    .then(response => {
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`); // Use backticks for template literals
      }
      return response.json();
    })
    .then(data => {
      // console.log("Data:", data);

      // Example of how you might display the holidays on the page
      const holidaysContainer = document.getElementById('holidaysContainer'); // Ensure you have an element with this ID in your HTML

      if (!holidaysContainer) {
        console.error('Element with ID "holidaysContainer" not found.');
        return;
      }

      // Clear any existing content
      holidaysContainer.innerHTML = '';

      // Generate HTML for each holiday
      data.forEach(holiday => {
        const holidayElement = document.createElement('div');
        holidayElement.className = 'holiday mt-2';

        console.log(holiday);


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
      console.log(response);


      gender = response.gender;
      console.log("gender in acc : ",gender);
      

      document.getElementById("userName").textContent = `${response.employeeName}`;

      // document.getElementById("Welcome-title").innerHTML = `<b>Welcome ${response.employeeName} </b>`;

      document.getElementById('employeeName').innerHTML = `<b>Name: ${response.employeeName} </b>`;
      document.getElementById('employeeId').innerHTML = `<b>Employee Id: ${response.employeeId} </b>`;
      document.getElementById('myMobileNumber').innerHTML = `<b>Mobile Number: ${response.mobileNumber} </b>`;
      document.getElementById('myEmailId').innerHTML = `<b>Email Id: ${response.emailId} </b>`;
      if (response.employeeName !== response.managerName) {
        document.getElementById('myMangerName').innerHTML = `<b>Reports to: ${response.managerName} </b>`;
      }

    })
    .catch(error => {
      console.error('Error:', error);
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
        window.location.href = 'http://localhost:8080/api/leave_management/index.html'; // Redirect on success
      } else {
        alert('Error logging out: ' + data.message);
      }
    })
    .catch(error => {
      console.error('Logout error:', error);
      alert('An error occurred during logout. Please try again.');
    });
}


function hideContainer() {
  const welcomeContainer = document.querySelector(".welcome-container");
  if (welcomeContainer) {
    welcomeContainer.style.display = "none"; // Hides the container
  }
}


function displayNoData(message) {
  const noDataDiv = document.createElement('div');
  noDataDiv.className = 'no-data';
  noDataDiv.innerHTML = `
      <h2>${message}</h2>
      <img id="noDataImage" src="assets/images/noDataFoundImage.png" alt="No Data Found" />
  `;
  return noDataDiv;
}
