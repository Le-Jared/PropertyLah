function sortTableAsc() {
    let table, rows, switching, i, x, y, shouldSwitch;
    table = document.getElementById("myTable");
    switching = true;
    /* Make a loop that will continue until no switching has been done: */
    while (switching) {
      switching = false;
      rows = table.rows;
      /* Loop through all table rows (except the first, which contains table headers): */
      for (i = 1; i < (rows.length - 1); i++) {
        shouldSwitch = false;
        /* Get the two elements you want to compare, one from current row and one from the next: */
        x = rows[i].getElementsByTagName("TD")[2]; // the 3rd column is the Price column
        y = rows[i + 1].getElementsByTagName("TD")[2];
        /* Check if the two rows should switch place, based on the direction, asc or desc: */
        if (Number(x.innerHTML.replace(/[^0-9.-]+/g,"")) > Number(y.innerHTML.replace(/[^0-9.-]+/g,""))) {
          shouldSwitch = true;
          break;
        }
      }
      if (shouldSwitch) {
        /* If a switch has been marked, make the switch and mark that a switch has been done: */
        rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
        switching = true;
      }
    }
}

function sortTableDesc() {
    let table, rows, switching, i, x, y, shouldSwitch;
    table = document.getElementById("myTable");
    switching = true;
    while (switching) {
      switching = false;
      rows = table.rows;
      for (i = 1; i < (rows.length - 1); i++) {
        shouldSwitch = false;
        x = rows[i].getElementsByTagName("TD")[2];
        y = rows[i + 1].getElementsByTagName("TD")[2];
        if (Number(x.innerHTML.replace(/[^0-9.-]+/g,"")) < Number(y.innerHTML.replace(/[^0-9.-]+/g,""))) {
          shouldSwitch = true;
          break;
        }
      }
      if (shouldSwitch) {
        rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
        switching = true;
      }
    }
}
  
function calculateTotalRent() {
    const table = document.getElementById("myTable");
    const rows = table.rows;
    let totalRent = 0;

    // Loop through all table rows (except the first, which contains table headers)
    for (let i = 1; i < rows.length; i++) {
      // Get the rent value from the 3rd column (index 2) of the current row
      const rentCell = rows[i].getElementsByTagName("TD")[2];
      // Extract the numeric rent value, convert it to a number and add it to the total
      totalRent += Number(rentCell.innerHTML.replace(/[^0-9.-]+/g,""));
    }

    // Create a new row
    const row = table.insertRow(-1);
    // Insert 5 cells for the new row
    for (let j = 0; j < 7; j++) {
      row.insertCell(j);
    }

    // Display "Total monthly rent" in the first cell and the total rent in the third cell
    row.cells[0].innerHTML = "Total Price:";
    row.cells[2].innerHTML = "$" + totalRent.toFixed(2);
}

// Call the function after the page has loaded
window.onload = calculateTotalRent;
