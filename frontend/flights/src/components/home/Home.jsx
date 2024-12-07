import React, { useState, useEffect } from 'react';
import { Table, Button, Card, Col, Row, Typography, Radio, Space, Modal,Form, Input, Dropdown  } from 'antd';
import './Home.css';
import GooglePlacesAutocomplete from 'react-google-places-autocomplete';
import { PlusOutlined, ProductOutlined, DeleteOutlined, RiseOutlined, PlusCircleOutlined } from '@ant-design/icons';
import BookmarkBorderIcon from '@mui/icons-material/BookmarkBorder';
import Map from '../map/Map'; // Import the Map component
import axios from 'axios';
import { DownOutlined } from '@ant-design/icons';


const { Title, Text } = Typography;


const Home = () => {
  const [weatherData, setWeatherData] = useState(null);
  const [history, setHistory] = useState([]);
  const [locations, setLocations] = useState([]);
  const [searchValue, setSearchValue] = useState(null);
  const [itinerary, setItinerary]  = useState(null);
  const [selectedValue, setSelectedValue] = useState('Weather');
  const [chatLoading, setChatLoading] = useState(false);
  const [optimizeLoading, setOptimizeLoading] = useState(false); 
  const [searchLoading, setSearchLoading] = useState(false); 
  const [form] = Form.useForm();
  const [open, setOpen] = useState(false);
  const [tripName, setTripName] = useState('');
  const [tripNames, setTripNames] = useState([]);
  const [updateTrips, setUpdateTrips] = useState(false);
  const [focusPoint, setFocusPoint] = useState(null);
  const map = new google.maps.Map(document.createElement('div'));
  const places = new google.maps.places.PlacesService(map);

  const [items, setItems] = useState([ {
          key: '1',
          label: 'My Trips',
          disabled: true,
        },
        {
          type: 'divider',
        },]);

  const options = [
    {
      label: 'Weather',
      value: 'Weather',
    },
    {
      label: 'Itinerary',
      value: 'Itinerary',
    },
  ];

  // get list of created trips
  useEffect(() => {
    async function fetchData() {
        const url = "http://localhost:8080/all";
        console.log("FETCHING", `${url}`)
        try {
          const response = await fetch(url);      
          if (response.ok) {
            const data = await response.json();
            setTripNames(data)
          } else{            
            console.error("Error:", response.status, await response.text());
          }
        } catch (error) {
          console.error('Error getting trip:', error.response?.data || error.message);
          throw error;
        }
    }
    fetchData();
  } , []);

  useEffect( () => {
    async function fetchData() {
      if (updateTrips){
        const url = "http://localhost:8080/all";
        console.log("FETCHING", `${url}`)
        try {
          const response = await fetch(url);      
          if (response.ok) {
            const data = await response.json();
            console.log(data)
            setTripNames(data)
            setUpdateTrips(false)
          } else{            
            console.error("Error:", response.status, await response.text());
          }
        } catch (error) {
          console.error('Error getting trip:', error.response?.data || error.message);
          throw error;
        }
      }
    }
    fetchData();
  } , [updateTrips]);

  useEffect(() => {
    const newTrips = [
      {
        key: '1',
        label: 'My Trips',
        disabled: true,
      },
      {
        type: 'divider',
      },
    ]

    tripNames.forEach((trip) => {
      if (trip != "DummyTrip"){
        newTrips.push({key: trip, label:trip})
      }

    })

    setItems(newTrips);
  } , [tripNames]);


  //change trip
  const onClick = async ({ key }) => {
    console.log(`Click on item ${key}`);
    const url = `http://localhost:8080/${key}`;
    console.log("FETCHING", `${url}`)

    try {
      const response = await fetch(url);      
      if (response.ok) {
        const data = await response.json();
        console.log(data)
        setTripName(key)
        setItinerary(null)
        const addresses = Object.keys(data);
        const newHist = []
        console.log(addresses)
        addresses.forEach((addr, i) => {
          console.log("BUILDING")

            const request = {
              query: addr,
              fields: ["name", "formatted_address", "place_id"],
            };
          
            // get short name
           places.textSearch(request, (results, status) => {
             if (status === google.maps.places.PlacesServiceStatus.OK && results && results.length > 0) {
                  console.log("Place Name:", results[0].name);
    const locationInfo = {
      addr: addr,
      shortName: results[0].name,
      start: i === 0,
      end: i === addresses.length - 1,
    };

    const place = { key: i, locationInfo: locationInfo };
    newHist.push(place);

    if (newHist.length === addresses.length) {
      const sortedHist = newHist.sort((a, b) => a.key - b.key);
      console.log(sortedHist);
      setHistory(sortedHist);
      setLocations(addresses);
    }
  } else if (status === google.maps.places.PlacesServiceStatus.ZERO_RESULTS) {
    console.warn(`No results found for address: ${addr}. Retrying with geocoder.`);
    const geocoder = new google.maps.Geocoder();

    geocoder.geocode({ address: addr }, (geoResults, geoStatus) => {
      if (geoStatus === google.maps.GeocoderStatus.OK && geoResults.length > 0) {
        console.log("Geocode Result:", geoResults[0]);
        const locationInfo = {
          addr: addr,
          shortName: geoResults[0].formatted_address,
          start: i === 0,
          end: i === addresses.length - 1,
        };

        const place = { key: i, locationInfo: locationInfo };
        newHist.push(place);

        if (newHist.length === addresses.length) {
          const sortedHist = newHist.sort((a, b) => a.key - b.key);
          console.log(sortedHist);
          setHistory(sortedHist);
          setLocations(addresses);
        }
      } else {
        console.error(`Geocode search failed for ${addr}:`, geoStatus);
      }
    });
  } else {
    console.error("Places search failed:", status);
  }
            });
        });
      } else
      console.error("Error:", response.status, await response.text());
    } catch (error) {
      console.error('Error getting trip:', error.response?.data || error.message);
      throw error;
    }
  };

  const handleSave = async (values) => {
    if (tripNames.includes(values.tripName)){
      alert("A trip with this name already exists")
    } else {
      setTripName(values.tripName)
      const url = `http://localhost:8080/trip/${values.tripName}`;
  
      try {
        const response = await axios.post(url, locations, {
          headers: {
            'Content-Type': 'application/json',
          },
        });
  
        console.log('Locations created:', response.data);
        setUpdateTrips(true)
        setOpen(false);
      } catch (error) {
        console.error('Error creating locations:', error.response?.data || error.message);
        throw error;
      }
    }
  };

  const handleOptimize = async () => {
    if (history){
      if (history.length < 20 && history.length > 2){
        const prevHistory = history
        let endFound = false
        let locations = prevHistory
        .sort((a, b) => {
          if (a.locationInfo.start) return -1;
          if (b.locationInfo.start) return 1;
          if (a.locationInfo.end) return 1;
          if (b.locationInfo.end) return -1;
          return 0;
        })
        
        locations.forEach((item, index) => {
          if (item.locationInfo.end && !index == locations.length){
            endFound = true
          } else if (!endFound && index == locations.length) {
            item.locationInfo.end = true;
          }
          item.key = index; // Update the key to start at 0
        });

        console.log("SORTED", locations)

      const addresses = locations.map(item => item.locationInfo.addr);


       console.log(addresses)

        const url = `http://localhost:8080/getroute?${addresses.map(addr => `address=${encodeURIComponent(addr)}`).join('&')}`;

        console.log("FETCHING", `${url}`)

        try {
          setOptimizeLoading(true)
          const response = await fetch(url);
          // Handle the response
          
          if (response.ok) {
            const json = await response.json();
            console.log(json); 
            const optimized = json.route
            const newHist = []
            const newLoc = []
 
            optimized.forEach(key => {
                console.log(key)
                const place = locations.find(item => {
                  return item.key === key;
                });
                console.log(place)
                newHist.push(place)
                newLoc.push(place.locationInfo.addr)
              });
  
            console.log("REARRANGED", optimized, newHist, newLoc)
            setHistory(newHist);
            setLocations(newLoc);
            setOptimizeLoading(false)
          } else {
            console.error("Error:", response.status, await response.text());
          }
        } catch (error) {
          console.error("Fetch error:", error);
        }
      }else{
        alert('A minimum of 3 and a maximum of 20 locations may be optimized.');
      }

    }else {
      alert('Please search for locations first.');
    }  
  }

  const handleChat = async () => {
    if (history.length > 0){
      const prevHistory = history
      const locations = prevHistory.map(item => item.locationInfo.addr);
      const addresses = locations.join(", ");

      const encodedPrompt = encodeURIComponent(addresses);

      console.log(encodedPrompt)

      const url = `http://localhost:8080/chat?prompt=${encodedPrompt}`;

      console.log("FETCHING", `${url}`)

      try {
        setChatLoading(true)
        const response = await fetch(url);
        // Handle the response
        
        if (response.ok) {
          let itinerary = await response.json();
          try {
            console.log("ITINERAY", itinerary);
            setItinerary(itinerary)
            setSelectedValue("Itinerary")
            setChatLoading(false)
          } catch (error) {
            console.error("Error parsing JSON:", error);
            alert("Something went wrong. Please try again later")
          }
        } else {
          console.error("Error:", response.status, await response.text());
        }
      } catch (error) {
        console.error("Fetch error:", error);
      }
    }else {
      alert('Please search for locations first.');
    }  
  }

  const handleSearch = async () => {
    if (searchValue) {
      if (!locations.includes(searchValue.label)){
        try {
          console.log(searchValue)
          const address = searchValue.label;
          const url = 'http://localhost:8080/getweather';
          const params = new URLSearchParams({
            address: address
          });
          console.log("FETCHING", `${url}?${params.toString()}`)
  
          setSearchLoading(true)
          const response = await fetch(`${url}?${params.toString()}`);
  
          if (response.ok) {
            const data = await response.json();
            const locationInfo = {'addr': address, 'shortName': searchValue.value.structured_formatting.main_text, 'start':false, 'end': false};
            
            if (history.length == 0){
              locationInfo.start = true;
            }
            setFocusPoint(address);
            setLocations ((prevLocations) => [
              ...prevLocations,
              address
            ]);
            setHistory((prevHistory) => [
              ...prevHistory,
              { key: prevHistory.length, locationInfo: locationInfo },
            ]);
  
            setWeatherData(data);
            setSearchLoading(false)
          } else {
            console.error(response.status, response.statusText)
            alert('Location not found. Please try again.');
            setSearchLoading(false)
  
          }
        } catch (error) {
          console.error('Error fetching location data:', error);
        }
      } else {
        alert('Location already entered')
      }
    }
    else{
      alert('Please search for a location first.');
    }
  };

  const changeLocation = async (address) => {
    try {      
      const url = 'http://localhost:8080/getweather';
      const params = new URLSearchParams({
        address: address
      });
      console.log("FETCHING", `${url}?${params.toString()}`)

      const response = await fetch(`${url}?${params.toString()}`);

      if (response.ok) {
        setFocusPoint(address)
        const data = await response.json();
        setWeatherData(data);
      } else {
        console.error(response.status, response.statusText)
        alert('Location not found. Please try again.');
      }
    } catch (error) {
      console.error('Error fetching weather data:', error);
    }
  };

  const removeLocation = (place) => {
    setLocations((prevLocations) =>
      prevLocations.filter((location) => location !== place,
        )
    );

    let prevHistory = history;
    prevHistory = prevHistory.filter((record) => record.locationInfo.addr !== place)
    prevHistory.forEach((item, index) => {
      item.key = index ;
    });

    setHistory(prevHistory);

  };

  const handleRadioChange = (key, field) => {
    const prevHistory = history
    const updatedData = prevHistory.map(item => {
      if (field === "start") {
        return {
          ...item,
          locationInfo: {
            ...item.locationInfo,
            start: item.key === key ? true : false,
            end: item.key === key ? false : item.locationInfo.end // When an item is set to start, end should be false
          }
        };
      }
  
      if (field === "end") {
        return {
          ...item,
          locationInfo: {
            ...item.locationInfo,
            start: item.key === key ? false: item.locationInfo.start, // When an item is set to end, start should be false
            end: item.key === key ? true : false
          }
        };
      }
  
      return {
        ...item,
        locationInfo: {
          ...item.locationInfo,
          start: false,
          end: false
        }
      };
    });

    console.log(updatedData)
  
    setHistory(updatedData);
  };
  
  const handleChange = (e) => {
    console.log('Selected option:', e.target.value); // Log the selected value
    setSelectedValue(e.target.value); // Update the state with the selected value
  };

  const showModal = () => {
    if (locations.length > 0){
      setOpen(true)
    } else{
      alert('Please add locations first');
    }
  };

  const columns = [
    {
      title: 'Location',
      dataIndex: 'locationInfo',
      key: 'location',
      render: (locationInfo) => <a onClick={() => changeLocation(locationInfo.addr)}>{locationInfo.shortName}</a>,

   
    },
    {
      title: 'Action',
      key: 'action',
      render: (_, record) => (
        <Button
          type="link"
          onClick={() => removeLocation(record.locationInfo.addr)}
        >
          Remove
        </Button>
      ),
    },
    {
      title: 'Start/End',
      key: 'Status',
      render: (_, record) => (
        <Radio.Group
        value={record.locationInfo.start ? "start" : record.locationInfo.end ? "end" : null}
        onChange={(e) => {
          if (e.target.value === "start") {
            handleRadioChange(record.key, "start");
          } else if (e.target.value === "end") {
            handleRadioChange(record.key, "end");
          }
        }}
      >
        <Radio value="start">Start</Radio>
        <Radio value="end">End</Radio>
      </Radio.Group>
      ),
    },
  ];

  const handleDeleteTrip = async () => {
    if (tripName){
      const url = `http://localhost:8080/delete/${tripName}`;
      try {
        // Send DELETE request
        const response = await axios.delete(url);
        handleNewTrip()
        setUpdateTrips(true);
        // Log success response
        console.log('Trip Deleted:', response.data);
    } catch (error) {
      console.error(error)
    }
    }
  };

  const handleNewTrip = () => {
    if (tripName){
      setTripName(null);
      setHistory([]);
      setLocations([]);
      setSelectedValue('Weather');
      setWeatherData(null);
      setItinerary(null);
    }
  };

  return (
    <div className="content-main">
       <Row justify="center" align="middle" style={{ width: '100%' }}>
      <Col>
        <Space>
          <div style={{ minWidth: '500px' }}>
            <GooglePlacesAutocomplete
              apiKey="AIzaSyATBzrG7fNTsR6yLYqUKVktQZL8huu44XU"
              debounce="500"
              selectProps={{
                searchValue,
                onChange: setSearchValue,
              }}
            />
          </div>
          
          <Button
            type="primary"
            shape="circle"
            icon={<PlusOutlined />}
            loading={searchLoading}
            onClick={() => handleSearch()}
          />
        </Space>
      </Col>
    </Row> 
    
    <Map locations={locations} focusPoint={focusPoint}/>


      <Row gutter={16}>
        <Col span={12}>

        {selectedValue === 'Weather' ? (
      <Card
        style={{
          width: '100%',
          borderRadius: '10px',
          boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
          backgroundColor: 'white',
        }}>
          {weatherData ? (
            <>
              <Title level={3} style={{ marginBottom: '10px' }}>
                Weather in {weatherData.name}
              </Title>
              <div style={{ textAlign: 'center' }}>
                <img
                  src={`https://openweathermap.org/img/wn/${weatherData.icon}@4x.png`}
                  alt={weatherData.description}
                  style={{ width: '110px', height: '110px' }}
                />
                <p style={{ fontSize: '18px' }}>
                  <Text strong style={{ fontSize: '18px' }}>Temperature:</Text> {weatherData.temperature}°C
                </p>
                <p style={{ fontSize: '18px' }}>
                  <Text strong style={{ fontSize: '18px' }}>Condition:</Text> {weatherData.description}
                </p>
              </div>
            </>
          ) : (
            <Text type="secondary">Nothing to show here, please search your location.</Text>
          )}
        </Card>
        ) : (
          <Card
            style={{
              width: '100%',
              borderRadius: '10px',
              boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
              backgroundColor: 'white',
            }}
          >
            {itinerary ? (
              <>
                <Title level={3} style={{ marginBottom: '10px' }}>
                  Sample Roadtrip Itinerary
                </Title>
                <div className="itinerary-container">
                  <div className="itinerary">
                    {itinerary.map((item, index) => {
                      if (item.days) {
                        return (
                          <div key={index} className="day-section" style={styles.daySection}>
                            <h2 style={styles.dayHeader}>{item.days}</h2>
                            <ul style={styles.activityList}>
                              {item.activities.map((activity, idx) => (
                                <li key={idx} style={styles.activityItem}>{activity}</li>
                              ))}
                            </ul>
                          </div>
                        );} 
                      })}
                  </div>
                </div>
              </>
            ) : (
              <Text type="secondary">Nothing to show here, please search for an itinerary.</Text>
            )}
          </Card>
      )}

        <div style={{ textAlign: 'center' }}>
            <Space direction="vertical" size="middle">
              <Radio.Group
                options={options}
                onChange={handleChange} 
                value={selectedValue}
                optionType="button"
                buttonStyle="solid"
              />
            </Space>
          </div>
        </Col>

        <Col span={12}>
          <Card
            style={{
              width: '100%',
              borderRadius: '10px',
              boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
              backgroundColor: 'white',
        
            }}
          >
          <Row justify="space-between" align="middle">
              <Col>
                <Title level={3}>Road Trip Locations</Title>
                {tripName? <div>

                  <h3> Current Trip: {tripName}</h3>
                  <Button type="primary" icon={<DeleteOutlined />}
                color="danger"
                    onClick={() => handleDeleteTrip()}
                    loading={optimizeLoading}
                    style={{
                      margin:'5px'
                    }}
                    >
                Delete trip
              </Button>
              <Button type="primary" icon={<PlusCircleOutlined />}
                color="danger"
                    onClick={() => handleNewTrip()}
                    loading={optimizeLoading}
                    style={{
                      margin:'5px'
                    }}
                    >
                New trip
              </Button>
                </div> : null}
               
              </Col>
              <Dropdown
                menu={{
                  items,
                  onClick,
                }}
                trigger={['click']}

              >
                <a onClick={(e) => e.preventDefault()}>
                  <Space>
                    Change trip
                    <DownOutlined />
                  </Space>
                </a>
              </Dropdown>
            </Row>            
            <Table
              columns={columns}
              dataSource={history}
              pagination={false}
              size="large"
              bordered
            />

            <div style={{ textAlign: 'center' }}>
              <Button type="primary" icon={<RiseOutlined /> }
                    onClick={() => handleOptimize()}
                    loading={optimizeLoading}
                    style={{
                      margin:'5px'
                    }}
                    >
                Optimize Route!
              </Button>

              <Button type="primary" icon={<ProductOutlined />}
                    onClick={() => handleChat()}
                    loading={chatLoading}
                    style={{
                      margin:'5px'
                    }}>
                Generate Itinerary!
              </Button>
            </div>
            <div style={{ textAlign: 'center' }}>

<Button type="primary" onClick={showModal}  icon={<BookmarkBorderIcon />}> 
        Save Trip!
      </Button>

      <Modal
        open={open}
        title="Save your trip"
        okText="Save"
        cancelText="Cancel"
        okButtonProps={{
          autoFocus: true,
          htmlType: 'submit',
          loading: false
        }}
        onCancel={() => setOpen(false)}
        destroyOnClose
        modalRender={(dom) => (
          <Form
            layout="vertical"
            form={form}
            name="form_in_modal"
            initialValues={{
              modifier: 'public',
            }}
            clearOnDestroy
            onFinish={(values) => handleSave(values)}
          >
            {dom}
          </Form>
        )}
      >
        <Form.Item
          name="tripName"
          label="Trip Name"
          rules={[
            {
              required: true,
              message: 'Please input the name of your trip!',
            },
          ]}
        >
          <Input />
        </Form.Item>
        {/* <Form.Item name="description" label="Description">
          <Input type="textarea" />
        </Form.Item> */}
      </Modal>
            </div>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

const styles = {
  daySection: {
    padding: '10px',
  },
  dayHeader: {
    fontSize: '24px',
    color: '#809fff',
    marginBottom: '10px',
  },
  activityList: {
    listStyleType: '-',
  },
  activityItem: {
    marginBottom: '10px',
    fontSize: '16px',
    color: '#333',
  },
};
export default Home;
