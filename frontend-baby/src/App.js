import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import SignInSide from "./sign-in-side/SignInSide";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<SignInSide />} />
      </Routes>
    </Router>
  );
}

export default App;
