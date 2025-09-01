import React, { createContext, useEffect, useState, useContext } from 'react';
import { getBebesByUsuario } from '../services/bebesService';
import { AuthContext } from './AuthContext';

export const BabyContext = createContext(null);

export function BabyProvider({ children }) {
  const [babies, setBabies] = useState([]);
  const [activeBaby, setActiveBaby] = useState(null);
  const { user } = useContext(AuthContext);

  useEffect(() => {
    const fetchBabies = async () => {
      if (!user?.id) return;
      try {
        const response = await getBebesByUsuario(user.id);
        const data = response.data || [];
        setBabies(data);
        setActiveBaby(data[0] || null);
      } catch (error) {
        console.error('Error fetching babies', error);
      }
    };

    fetchBabies();
  }, [user]);

  const addBaby = (baby) => {
    setBabies((prev) => [...prev, baby]);
    setActiveBaby(baby);
  };

  const removeBaby = (id) => {
    setBabies((prev) => {
      const updatedBabies = prev.filter((baby) => baby.id !== id);
      if (activeBaby && activeBaby.id === id) {
        setActiveBaby(updatedBabies[0] || null);
      }
      return updatedBabies;
    });
  };

  return (
    <BabyContext.Provider value={{ babies, activeBaby, setActiveBaby, addBaby, removeBaby }}>
      {children}
    </BabyContext.Provider>
  );
}
