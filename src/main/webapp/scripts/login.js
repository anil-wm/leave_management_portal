
// document.getElementById("login-btn").addEventListener('click', function(event) {
    
//     event.preventDefault(); // Prevent the default form submission
    
//     // Call the login function
//     login();
// });



// //  login 

// function login() {
//     const emailId = document.getElementById("emailId").value.trim();
//     const password = document.getElementById("password").value.trim();
  
//     console.log(emailId,password);
  
//     const loginCredentials = {
//         emailId,
//         password
//     };
  
//     console.log(loginCredentials);
  
//     // Encode credentials for Basic Authentication
//     // const encodedCredentials = btoa(`${emailId}:${password}`);
  
//     fetch("http://localhost:8080/leave_management/login", {
//         method: "POST",
//         headers: {
//             "Content-Type": "application/json",
//             // "Authorization": `Basic ${encodedCredentials}`,
//         },
//         body: JSON.stringify(loginCredentials),
//     })
//     .then(async (response) => {
//         if (!response.ok) {
//             const text = await response.json();
//             throw new Error(`Network response was not ok: ${text}`);
//         }
//         return response.json();
//     })
//     .then((data) => {
//         console.log("Success:", data);
//         loadHolidays(data);
//     })
//     .catch((error) => {
//         console.error("Error:", error);
//         alert("There was an error processing your request. " + error.message);
//     });
//   }
  
  
  // document.addEventListener('DOMContentLoaded', function() {
  
  

  function login() {
    const emailId = document.getElementById("emailId").value.trim();
    const password = document.getElementById("password").value.trim();

    console.log(emailId, password);

    const loginCredentials = {
        emailId : emailId,
        password : password
    };

    console.log(loginCredentials);

    fetch("http://localhost:8080/api/leave_management/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            // "Authorization": `Basic ${encodedCredentials}`,
        },
        body: JSON.stringify(loginCredentials),
    })
    .then(async (response) => {
        const text = await response.json(); 
        console.log("Raw response:", text); 

        if (!response.ok) {
            throw new Error(`Network response was not ok: ${text+"hello"}`);
        }
    })
    .then((data) => {
        console.log("Success:", data);
    })
    .catch((error) => {
        console.error("Error:", error);
        
    });
}





function loadHolidays(data) {


    fetch("http://localhost:8080/leave_management/holidays",{
        method : "GET",
        headers : "application/json",
    }).then(response =>{
        if (!response.ok) {
            throw new Error('HTTP error ! Status : ${response.status}');
        }
        return response.json();
    })
    .then(data =>{
        console.log("Data : ",data);
        
    } )
    .catch(error =>{
        console.error('Error : ',error);
    })

    // console.log(data);

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
        holidayElement.className = 'holiday';

        holidayElement.innerHTML = `
            <h3>${holiday.holidayName}</h3>
            <p>${holiday.day}</p>
            <p>${holiday.holidayDate}</p>
        `;

        holidaysContainer.appendChild(holidayElement);
    });
}
