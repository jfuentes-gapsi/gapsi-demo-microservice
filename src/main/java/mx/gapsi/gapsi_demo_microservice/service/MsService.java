package mx.gapsi.gapsi_demo_microservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.gapsi.commons.model.Base;
import mx.gapsi.gapsi_demo_microservice.dao.MsDao;

@Service
public class MsService {

    @Autowired
    private MsDao msDao;

    public Base findAll (Base base) {
        msDao.findAll(base);
        return base;
    }
    
}
