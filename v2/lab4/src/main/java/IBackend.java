import DTO.CityDTO;
import DTO.CountryDTO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IBackend extends Remote {
    public CountryDTO countryFindById(Long id) throws RemoteException;

    public CityDTO cityFindByName(String name) throws RemoteException;

    public CountryDTO countryFindByName(String name) throws RemoteException;

    public boolean cityUpdate(CityDTO city) throws RemoteException;

    public boolean countryUpdate(CountryDTO country) throws RemoteException;

    public boolean cityInsert(CityDTO city) throws RemoteException;

    public boolean countryInsert(CountryDTO country) throws RemoteException;

    public boolean countryDelete(CountryDTO country) throws RemoteException;

    public boolean cityDelete(CityDTO city) throws RemoteException;

    public List<CountryDTO> countryAll() throws RemoteException;

    public List<CityDTO> cityAll() throws RemoteException;

    public List<CityDTO> cityFindByCountryId(Long idc) throws RemoteException;
}
