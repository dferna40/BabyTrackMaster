import React, { createContext, useEffect, useState } from 'react';
import { getBebes } from '../services/bebesService';

export const BabyContext = createContext(null);

export function BabyProvider({ children }) {
  const [babies, setBabies] = useState([]);
  const [activeBaby, setActiveBaby] = useState(null);

  useEffect(() => {
    const fetchBabies = async () => {
      try {
        const response = await getBebes();
        const data = response.data || [];
        setBabies(data);
        setActiveBaby(data[0] || null);
      } catch (error) {
        console.error('Error fetching babies', error);
      }
    };

    fetchBabies();
  }, []);

  const addBaby = (baby) => {
    setBabies((prev) => [...prev, baby]);
    setActiveBaby(baby);
  };

  return (
    <BabyContext.Provider value={{ babies, activeBaby, setActiveBaby, addBaby }}>
      {children}
    </BabyContext.Provider>
  );
}
