import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import CuidadoForm from './CuidadoForm';
import { listarTipos, listarTiposPanal } from '../../services/cuidadosService';
import { LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';

jest.mock('../../services/cuidadosService', () => ({
  listarTipos: jest.fn(),
  listarTiposPanal: jest.fn(),
}));

describe('CuidadoForm', () => {
  beforeEach(() => {
    listarTipos.mockResolvedValue({ data: [{ id: 1, nombre: 'Pañal' }] });
    listarTiposPanal.mockResolvedValue({
      data: [
        { id: 10, nombre: 'Pipi' },
        { id: 11, nombre: 'Caca' },
        { id: 12, nombre: 'Mixto' },
      ],
    });
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('renderiza selector de tipo de pañal y envía tipoPanalId y cantidadPanal', async () => {
    const onSubmit = jest.fn();
    render(
      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <CuidadoForm
          open
          onClose={() => {}}
          onSubmit={onSubmit}
          initialData={{ inicio: '2024-01-01T10:00' }}
        />
      </LocalizationProvider>
    );

    await waitFor(() => expect(listarTipos).toHaveBeenCalled());

    const tipoSelect = screen.getAllByRole('combobox')[0];
    await userEvent.click(tipoSelect);
    await userEvent.click(screen.getByRole('option', { name: 'Pañal' }));

    await waitFor(() => {
      expect(screen.getAllByRole('combobox').length).toBe(2);
    });
    const tipoPanalSelect = screen.getAllByRole('combobox')[1];
    await userEvent.click(tipoPanalSelect);
    await userEvent.click(screen.getByRole('option', { name: 'Pipi' }));

    const spinbuttons = screen.getAllByRole('spinbutton');
    const cantidadInput = spinbuttons[spinbuttons.length - 1];
    await userEvent.type(cantidadInput, '2');

    await userEvent.click(screen.getByRole('button', { name: 'Guardar' }));

    await waitFor(() =>
      expect(onSubmit).toHaveBeenCalledWith(
        expect.objectContaining({ tipoPanalId: 10, cantidadPanal: '2' })
      )
    );
  });

  it('envía la fecha en formato ISO al guardar', async () => {
    const onSubmit = jest.fn();
    render(
      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <CuidadoForm
          open
          onClose={() => {}}
          onSubmit={onSubmit}
          initialData={{ inicio: '2024-01-01T10:00:00Z', tipoId: 1 }}
        />
      </LocalizationProvider>
    );

    await waitFor(() => expect(listarTipos).toHaveBeenCalled());

    await userEvent.click(screen.getByRole('button', { name: 'Guardar' }));

    const expected = new Date('2024-01-01T10:00:00.000Z').toISOString();
    await waitFor(() =>
      expect(onSubmit).toHaveBeenCalledWith(
        expect.objectContaining({ inicio: expected })
      )
    );
  });
});
