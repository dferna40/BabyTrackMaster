import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import SignInSide from "./sign-in-side/SignInSide";
import SignUp from "./sign-up/SignUp";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<SignInSide />} />
        <Route path="/" element={<SignUp />} />
      </Routes>
    </Router>
  );
}

export default App;
