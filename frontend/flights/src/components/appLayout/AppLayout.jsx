import React from 'react';
import Nav from '../nav/Nav';
import Footer from '../footer/Footer';

const AppLayout = ({ children }) => {
  return (
    <>
      <Nav />
      <div className="content-wrapper">{children}</div>
      <Footer />
    </>
  );
};

export default AppLayout;
