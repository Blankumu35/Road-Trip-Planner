import React, { useEffect, useRef } from 'react';

const Map = ({ locations, focusPoint }) => {
  const mapRef = useRef(null);
  const mapInstance = useRef(null);
  const markerRef = useRef(null);
  
  useEffect(() => {
    // Initialize the map if not already initialized
    if (!mapInstance.current) {
      mapInstance.current = new google.maps.Map(mapRef.current, {
        center: { lat: -33.8688, lng: 151.2195 }, // Default center
        zoom: 13,
        mapTypeId: 'roadmap',
      });
    }

    // Clear markers
    if (markerRef.current){
        markerRef.current.forEach(marker => marker.setMap(null));
    }
    markerRef.current = [];

    // If a search term exists, geocode it to update map location
    if (locations) {
      const geocoder = new google.maps.Geocoder();
      
      locations.forEach((addr, i) => {
        geocoder.geocode({ address: addr }, (results, status) => {
            if (status === 'OK') {
              const location = results[0].geometry.location;
    
              if ( i == locations.length-1){
                    // set to most recent
                    mapInstance.current.setCenter(location);
                    mapInstance.current.setZoom(13);
                }

              const marker = new google.maps.Marker({
                map: mapInstance.current,
                position: location,
                title: addr,
                label: {
                  text: `${i + 1}`,
                  color: 'white',
                  fontSize: '12px',
                  fontWeight: 'bold',
                },
              });

              markerRef.current.push(marker); 
            } else {
              console.error('Geocode was not successful for the following reason: ' + status);
            }
          });
      });
    }
  }, [locations]);

  useEffect(() => {
    // Initialize the map if not already initialized
    if (!mapInstance.current) {
      mapInstance.current = new google.maps.Map(mapRef.current, {
        center: { lat: -33.8688, lng: 151.2195 }, // Default center
        zoom: 13,
        mapTypeId: 'roadmap',
      });
    }
    // If a search term exists, geocode it to update map location
    if (focusPoint) {
      const geocoder = new google.maps.Geocoder();
      
   
        geocoder.geocode({ address: focusPoint }, (results, status) => {
            if (status === 'OK') {
              const location = results[0].geometry.location;
    
              // set to new address
              mapInstance.current.setCenter(location);
              mapInstance.current.setZoom(13);
                
            } else {
              console.error('Geocode was not successful for the following reason: ' + status);
            }
          });
    }
  }, [focusPoint]);

  return (
    <div
      ref={mapRef}
      style={{
        height: '400px',  // Set your desired height for the map
        width: '100%',
        borderRadius: '10px',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
        marginBottom: '10px'
      }}
    ></div>
  );
};

export default Map;
