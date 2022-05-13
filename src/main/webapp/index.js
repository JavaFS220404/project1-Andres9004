const url = "http://localhost:8080/ers/";

//Login functions
let loginbtn = document.getElementById("loginBtn");
loginbtn.addEventListener("click", login);

function registerNewUser() {
  let createUser = document.getElementById("createUser");
  //let userLoginForm = document.getElementById("userLoginForm");
  if (createUser.style.display === "none") {
    createUser.style.display = "block";
    //userLoginForm.style.display="none"
  } else {
    createUser.style.display = "none";
    //userLoginForm.style.display = "block";
  }
}

async function login() {
  let uName = document.getElementById("username").value;
  let pWord = document.getElementById("password").value;

  let user = {
    username: uName,
    password: pWord
  };
  console.log(user);

  let response = await fetch(url + "login", {
    method: "POST",
    body: JSON.stringify(user),
    credentials: "include"
  });

  if (response.status === 200) {
    console.log("logged in");
    let selectedRole = document.getElementById("thisUserRole").value;
    let userRole = parseInt(selectedRole);
    showLoginMenu();

    if (userRole ==1){
      showUserMenu();
    }
    if (userRole==2){
      showManagerMenu();
    }

  } else {
    console.log("Could not log in");
    console.log(response);
  }
}

async function UserLogout() {


  let response = await fetch(url + "logout", {
    method: "GET",
    credentials: "include"
  });

  if (response.status === 200) {
    console.log("logged out");
    showLoginMenu();
    hideUserAndManagerMenu();

  } else {
    console.log("Could not log out");
    console.log(response);
  }
}

//user functions
let userButton = document.getElementById("getUsers");
let newUserBtn = document.getElementById("userBtn");


async function addUser() {
  let newUser = gatherUser();

  let response = await fetch(url+"users",
    {
      method: "POST",
      body: JSON.stringify(newUser)
    })
  if (response.status === 201) {
    console.log("User added successfully.")
    registerNewUser();
  } else {
    console.log("Something went wrong adding the user")
    console.log(response)
  }

}

function gatherUser() {
  let newUsername = document.getElementById("newUsername").value;
  let newPassword = document.getElementById("newPassword").value;
  let newFirstName = document.getElementById("newFirstName").value;
  let newLastName = document.getElementById("newLastName").value;
  let newEmail = document.getElementById("newEmail").value;
  let newPhone = document.getElementById("newPhone").value;
  let newAddress = document.getElementById("newAddress").value;

  let user = {
    id: 0,
    username: newUsername,
    password: newPassword,
    firstName: newFirstName,
    lastName: newLastName,
    email: newEmail,
    phoneNumber: newPhone,
    address: newAddress

  }

  return user;
}


function showUsers() {
  let usersTable = document.getElementById("usersTable");
  HideAllFuncs();
  if (usersTable.style.display === "none") {
    usersTable.style.display = "block";
    fetchUsers();
    console.log("fetching users");
  } else {
    usersTable.style.display = "none";
  }
}

function HideAllFuncs(){
  let usersTable = document.getElementById("usersTable");
  usersTable.style.display = "none";
  let mticketsTable = document.getElementById("managerTicketsTable");
  mticketsTable.style.display = "none";
  let processReimb = document.getElementById("processReimb");
  processReimb.style.display = "none";
  let uticketsTable = document.getElementById("userTicketsTable");
  uticketsTable.style.display = "none";
  let ncreateTicket = document.getElementById("createTicket");
  ncreateTicket.style.display = "none";
}

async function fetchUsers() {

  let response = await fetch("http://localhost:8080/ers/users");

  if (response.status === 200) { //request successful
    let data = await response.json();

    console.log(data);
    populateUsers(data);
  } else {
    console.log("Could not find the users.")
  }
}

function populateUsers(users) {
  let tbody = document.getElementById("userBody");
  tbody.innerHTML = "";
  for (let user of users) {
    let row = document.createElement('tr');
    for (let cell in user) {
      if (cell != "password") {
        let td = document.createElement('td');
        td.innerText = user[cell];
        row.appendChild(td);
      }
    }
    tbody.appendChild(row);
  }
}


//Ticket functions
let addTicketBtn = document.getElementById("createTicketBtn");
addTicketBtn.addEventListener("click", addReimb);

let processTicketBtn = document.getElementById("ProcessTicketBtn");
processTicketBtn.addEventListener("click", updateReimb);

function showUserTickets() {
  let ticketsTable = document.getElementById("userTicketsTable");
  HideAllFuncs();
  if (ticketsTable.style.display === "none") {
    ticketsTable.style.display = "block";
    fetchTickets();
    console.log("fetching tickets");
  } else {
    ticketsTable.style.display = "none";
  }
}

function showManagerTickets() {
  let ticketsTable = document.getElementById("managerTicketsTable");
  HideAllFuncs();
  if (ticketsTable.style.display === "none") {
    ticketsTable.style.display = "block";
    fetchAllTickets();
    console.log("fetching tickets");
  } else {
    ticketsTable.style.display = "none";
  }
}

function showNewTicketForm() {
  let createTicket = document.getElementById("createTicket");
  HideAllFuncs();
  if (createTicket.style.display === "none") {
    createTicket.style.display = "block";
  } else {
    createTicket.style.display = "none";
  }
}


function showProcessReimb() {
  let processReimb = document.getElementById("processReimb");
  HideAllFuncs();
  if (processReimb.style.display === "none") {
    processReimb.style.display = "block";
  } else {
    processReimb.style.display = "none";
  }
}



async function fetchTickets() {

  let response = await fetch("http://localhost:8080/ers/reimb");

  if (response.status === 200) { //request successful
    let data = await response.json();

    console.log(data);
    populateTickets(data);
  } else {
    console.log("Could not find the reimbursements.")
  }
}

async function fetchAllTickets() {

  let response = await fetch("http://localhost:8080/ers/reimb");

  if (response.status === 200) { //request successful
    let data = await response.json();

    console.log(data);
    populateAllTickets(data);
  } else {
    console.log("Could not find the reimbursements.")
  }
}

function populateTickets(reimbs) {
  let tbody = document.getElementById("reimbBody");
  tbody.innerHTML = "";
  for (let reimb of reimbs) {
    let row = document.createElement('tr');
    for (let cell in reimb) {
      let td = document.createElement('td');
      if (cell == "creationDate" || cell == "resolutionDate") {
        if (reimb[cell]) {
          let numDate = new Date(reimb[cell]);
          td.innerText = numDate.toLocaleDateString();
          row.appendChild(td);
        } else {
          td.innerText = reimb[cell];
          row.appendChild(td);
        }
      } else if (cell == "author" || cell == "resolver") {
        if (reimb[cell]) {
          td.innerText = reimb[cell]["firstName"] + " " + reimb[cell]["lastName"];
          row.appendChild(td);
        } else {
          td.innerText = reimb[cell];
          row.appendChild(td);
        }
      } else {
        td.innerText = reimb[cell];
        row.appendChild(td); //adds td to row
      }
    }
    tbody.appendChild(row); //adds row to tbody
  }
}

function populateAllTickets(reimbs) {
  let tbody = document.getElementById("managerTicketTableBody");
  tbody.innerHTML = "";
  for (let reimb of reimbs) {
    let row = document.createElement('tr');
    for (let cell in reimb) {
      let td = document.createElement('td');
      if (cell == "creationDate" || cell == "resolutionDate") {
        if (reimb[cell]) {
          let numDate = new Date(reimb[cell]);
          td.innerText = numDate.toLocaleDateString();
          row.appendChild(td);
        } else {
          td.innerText = reimb[cell];
          row.appendChild(td);
        }
      } else if (cell == "author" || cell == "resolver") {
        if (reimb[cell]) {
          td.innerText = reimb[cell]["firstName"] + " " + reimb[cell]["lastName"];
          row.appendChild(td);
        } else {
          td.innerText = reimb[cell];
          row.appendChild(td);
        }
      } else {
        td.innerText = reimb[cell];
        row.appendChild(td); //adds td to row
      }
    }
    tbody.appendChild(row); //adds row to tbody
  }
}

async function addReimb() {
  let newTicket = gatherReimb();
  console.log(JSON.stringify(newTicket));
  let response = await fetch(url+"reimb",
    {
      method: "POST",
      body: JSON.stringify(newTicket),
      credentials: "include"
    })
  if (response.status === 201) {
    console.log("Ticket added successfully.");
    HideAllFuncs();
    fetchTickets();
    let ticketsTable = document.getElementById("userTicketsTable");
    ticketsTable.style.display = "block";

  } else {
    console.log("Something went wrong adding the ticket");
    console.log(response);
  }

}

function gatherReimb() {
  let newTicketType = document.getElementById("ticketType").value;
  let newTicketAmount = document.getElementById("inputAmount").value;
  let newTicketDescription = document.getElementById("ticketDescription").value;

  let reimb = {
    amount: Number(newTicketAmount),
    description: newTicketDescription,
    ticketTypeId: parseInt(newTicketType)
  }
  console.log(reimb);
  return reimb;
}

async function updateReimb() {
  let ticketToUpdate = gatherUpdateTicket();
  console.log(JSON.stringify(ticketToUpdate));

  let response = await fetch(url + "reimb",
    {
      method: "PUT",
      body: JSON.stringify(ticketToUpdate),
      credentials: "include"
    })
  if (response.status === 201) {
    console.log("Ticket processed successfully.");
    HideAllFuncs();
    fetchTickets();
    let mTicketsTable = document.getElementById("managerTicketsTable");
    mTicketsTable.style.display = "block";

  } else {
    console.log("Something went wrong processing the ticket")
    console.log(response)
  }

}

function gatherUpdateTicket() {
  let ticketId = document.getElementById("reimbID").value;
  let selectedStatus = document.getElementById("status").value;

  let ticket = {
    id: parseInt(ticketId),
    newStatus: parseInt(selectedStatus)
  }
  console.log(ticket);
  return ticket;
}


function showLoginMenu() {
  let loginMenu = document.getElementById("loginMenu");
  if (loginMenu.style.display === "none") {
    loginMenu.style.display = "block";
  } else {
    loginMenu.style.display = "none";
  }
}

function showUserMenu() {
  let userMenu = document.getElementById("userMenu");
  if (userMenu.style.display === "none") {
    userMenu.style.display = "block";
  } else {
    userMenu.style.display = "none";
  }
}

function showManagerMenu() {
  let managerMenu = document.getElementById("managerMenu");
  if (managerMenu.style.display === "none") {
    managerMenu.style.display = "block";
  } else {
    managerMenu.style.display = "none";
  }
}

function hideUserAndManagerMenu(){
  let userMenu = document.getElementById("userMenu");
  userMenu.style.display = "none";
  let managerMenu = document.getElementById("managerMenu");
  managerMenu.style.display = "none";
}
