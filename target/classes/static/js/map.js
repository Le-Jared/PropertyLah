function initMap() {
  // Map options
  let options = {
    zoom: 11,
    center: { lat: 1.3521, lng: 103.8198 }, // Centered at Singapore
  }

  // New map
  let map = new google.maps.Map(document.getElementById('map'), options);

  // Get properties from server
  fetch('/api/properties')
    .then(response => {
        if (!response.ok) {
            throw new Error('HTTP error ' + response.status);
        }
        return response.json();
    })
    .then(properties => {
        // Loop through properties and add markers
        for (let i = 0; i < properties.length; i++) {
            addMarker(properties[i]);
            addSidebarItem(properties[i], i);
        }
    })
    .catch(error => {
        console.log('Failed to fetch properties:', error);
    });

  // Add Marker Function
  function addMarker(property) {
    let marker = new google.maps.Marker({
      position: { lat: property.latitude, lng: property.longitude },
      map: map,
    });

    // Format price to include commas and dollar sign
    let formattedPrice = "$" + property.price.toLocaleString();

    // Create info window
    let infowindow = new google.maps.InfoWindow({
      content: `
        <h3>${property.type}</h3>
        <p>Price: ${formattedPrice}</p>
        <p>Location: ${property.location}</p>
        <p>Description: ${property.description}</p>
        <button onclick="window.open('https://www.google.com/maps/dir//${property.latitude},${property.longitude}', '_blank')">Get Directions</button>
      `,
    });

    // Add click listener to marker for info window
    marker.addListener('click', function() {
      infowindow.open(map, marker);
    });
  }

  // Add Sidebar Item Function
  function addSidebarItem(property, index) {
    let sidebar = document.getElementById('sidebar');
    let item = document.createElement('div');
    item.className = 'sidebar-item';
    item.innerHTML = `<h5>${property.type}</h5><p>${property.location}</p>`;
    item.onclick = function() {
      map.setCenter({ lat: property.latitude, lng: property.longitude });
      map.setZoom(14);
    };
    sidebar.appendChild(item);
  }
}


