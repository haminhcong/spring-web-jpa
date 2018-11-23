package com.spring.service.service;

import com.spring.service.exception.AppException;
import com.spring.service.exception.CommonException;
import org.springframework.stereotype.Service;

@Service
public class AddrSingletonExceptionService {

  private int counter;

  public AddrSingletonExceptionService() {
    this.counter = 0;
  }

  public int getCounter(int type) throws Exception {
    counter += 1;
    try {
      if (type == 1) {
        throw new AppException("app exception");
      } else if (type == 2) {
        throw new CommonException("common exception");
      } else {
        return counter;
      }
    } catch (AppException appEx) {
      throw appEx;
    } catch (Exception ex) {
      throw new Exception("generic Exception" + ex.getMessage());
    }
  }

}
