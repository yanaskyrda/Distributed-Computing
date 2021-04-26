import DTO.CityDTO;
import DTO.CountryDTO;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Backend extends UnicastRemoteObject implements IBackend {

    protected Backend() throws RemoteException {
        super();
    }

    @Override
    public CountryDTO countryFindById(Long id) throws RemoteException {
        return CountryDAO.findById(id);
    }

    @Override
    public CityDTO cityFindByName(String name) throws RemoteException {
        return CityDAO.findByName(name);
    }

    @Override
    public CountryDTO countryFindByName(String name) throws RemoteException {
        return CountryDAO.findByName(name);
    }

    @Override
    public boolean cityUpdate(CityDTO city) throws RemoteException {
        return CityDAO.update(city);
    }

    @Override
    public boolean countryUpdate(CountryDTO country) throws RemoteException {
        return CountryDAO.update(country);
    }

    @Override
    public boolean cityInsert(CityDTO city) throws RemoteException {
        return CityDAO.insert(city);
    }

    @Override
    public boolean countryInsert(CountryDTO country) throws RemoteException {
        return CountryDAO.insert(country);
    }

    @Override
    public boolean countryDelete(CountryDTO country) throws RemoteException {
        return CountryDAO.delete(country);
    }

    @Override
    public boolean cityDelete(CityDTO city) throws RemoteException {
        return CityDAO.delete(city);
    }

    @Override
    public List<CountryDTO> countryAll() throws RemoteException {
        return CountryDAO.findAll();
    }

    @Override
    public List<CityDTO> cityAll() throws RemoteException {
        return CityDAO.findAll();
    }

    @Override
    public List<CityDTO> cityFindByCountryId(Long countryId) throws RemoteException {
        return CityDAO.findByCountryId(countryId);
    }

    public static void main(String[] args) throws RemoteException {
        var backend = new Backend();
        Registry r = LocateRegistry.createRegistry(8085);
        r.rebind("worldmap24", backend);
    }
}
