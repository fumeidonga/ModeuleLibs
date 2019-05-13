package com.rebeau.technology.java.testdesignmodel.factory.simplefactory;

import com.rebeau.technology.java.testdesignmodel.module.BiJiBenComputer;
import com.rebeau.technology.java.testdesignmodel.module.Computer;
import com.rebeau.technology.java.testdesignmodel.module.TaiShiJiComputer;

public class SimpleFactory {



    public static Computer getComputer(int type){
        switch (type) {
            case 0:
                return new TaiShiJiComputer();
            case 1:
                return new BiJiBenComputer();
        }
        return new Computer();
    }


}
