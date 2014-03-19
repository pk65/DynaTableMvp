package com.google.gwt.sample.dynatablemvp.server.svc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gwt.sample.dynatablemvp.server.dao.AddressDao;
import com.google.gwt.sample.dynatablemvp.server.domain.Address;

@Service
public class AddressService {
 
    public AddressService() {
	}

	private AddressDao addressDao;
 
    public AddressDao getAddressDao() {
        return addressDao;
    }
    @Autowired
    public void setAddressDao(AddressDao addressDao) {
        this.addressDao = addressDao;
    }
 
    public List<Address> fetchAllAddresses() {
        return getAddressDao().selectAll();
    }
    
    public Address find(Integer id){
    	return getAddressDao().findByPrimaryKey(id);
    }
    
    public Integer persist(Address address) {
		final String properName = Address.class.getName();
    	
		if(address!=null){
			String clazzName = address.getClass().getName();
			if(clazzName.equals(properName)){
		   		getAddressDao().insert(address,address.getId()!=null);
		    	return address.getId();
			}
		}
		return null;
    }
}