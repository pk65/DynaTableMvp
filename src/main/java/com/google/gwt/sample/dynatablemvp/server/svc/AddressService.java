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

    @Autowired
	private AddressDao addressDao;
  
    public List<Address> fetchAllAddresses() {
        return addressDao.selectAll();
    }
    
    public Address find(Integer id){
    	return addressDao.findByPrimaryKey(id);
    }
    
    public Integer persist(Address address) {
		final String properName = Address.class.getName();
    	
		if(address!=null){
			String clazzName = address.getClass().getName();
			if(clazzName.equals(properName)){
				addressDao.insert(address,address.getId()!=null,false);
		    	return address.getId();
			}
		}
		return null;
    }
}