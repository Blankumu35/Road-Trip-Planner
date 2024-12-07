import React from 'react';
import { Card, Col, Row, Typography } from 'antd';

const { Title, Text } = Typography;

const WeatherDisplay = ({ weatherData }) => {
  return (
    <div style={{ width: '100%', paddingTop:'20px' }}>
      <Card
        style={{
          width: '100%',
          borderRadius: '10px',
          boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
          backgroundColor: 'FFFFF0',
      padding:'20px'
        }}
      >
        <Row gutter={16}>
          <Col span={24}>
            <Title level={3} style={{ margin: 0 }}>
              Weather in {weatherData.name}
            </Title>
          </Col>
          <Col span={24} style={{ textAlign: 'center' }}>
            <img
              src={`https://openweathermap.org/img/wn/${weatherData.weather[0].icon}@2x.png`}
              alt={weatherData.weather[0].description}
              style={{ width: '80px', height: '80px' }}
            />
          </Col>
          <Col span={12}>
            <Text strong>Temperature:</Text>
            <p>{weatherData.main.temp}°C</p>
          </Col>
          <Col span={12}>
            <Text strong>Condition:</Text>
            <p>{weatherData.weather[0].description}</p>
          </Col>
        </Row>
      </Card>
    </div>
  );
};

export default WeatherDisplay;
