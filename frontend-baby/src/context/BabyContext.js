import React, { createContext, useState } from 'react';

export const BabyContext = createContext(null);

export function BabyProvider({ children }) {
  const initialBabies = [
    { id: 1, nombre: 'Carlos', fechaNacimiento: '2023-01-01' },
    { id: 2, nombre: 'Lucía', fechaNacimiento: '2022-09-15' },
  ];

  const [babies] = useState(initialBabies);
  const [activeBaby, setActiveBaby] = useState(initialBabies[0]);

  return (
    <BabyContext.Provider value={{ babies, activeBaby, setActiveBaby }}>
      {children}
    </BabyContext.Provider>
  );
}
