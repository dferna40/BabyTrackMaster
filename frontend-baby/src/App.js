import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import signInSide from "./sign-in-side";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<signInSide />} />
      </Routes>
    </Router>
  );
}

export default App;
