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
    for (let j = 0; j < 5; j++) {
        row.insertCell(j);
    }

    // Display "Total monthly rent" in the first cell and the total rent in the third cell
    row.cells[0].innerHTML = "Total Monthly Rent:";
    row.cells[2].innerHTML = "$" + totalRent.toFixed(2);
}

// Call the function after the page has loaded
window.onload = calculateTotalRent;
