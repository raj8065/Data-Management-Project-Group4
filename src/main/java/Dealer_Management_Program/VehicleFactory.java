package Dealer_Management_Program;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

public class VehicleFactory {

    private final static String[] colors = {"Red", "Blue", "Green", "Brown", "Gray", "White", "Black", "Yellow"};
    private final static int minYear = 2000;
    private final static int maxYear = 2019;
    private final static String[] engines = {"V8", "V6", "I4", "I4 Diesel"};
    private final static String[] transmissions = {"Automatic", "Manual"};

    /**
     * newRandomVehicles
     * creates new vehicles with valid random attributes
     * @param n numer of vehicles to create
     * @return the number of vehicles successfully added
     */
    public static int newRandomVehicles(CommandConstructor cc, int n){
        int count = 0;
        try {
            ResultSet r = cc.useQuery("SELECT MAX(VIN) FROM vehicle");
            int max = 0;
            int amnt = r.getMetaData().getColumnCount();
            while(r.next()){
                for(int i = 1; i <= amnt; i++){
                    max += Integer.parseInt(r.getObject(i).toString());
                    //System.out.println(r.getObject(i).toString());
                }
            }
            for(int i = 1; i <= n; i++) {
                String brand = genBrand(cc);
                String model = genModel(cc, brand);
                String bodyStyle = genBodyStyle(cc, model);
                String color = colors[(int) Math.floor(Math.random() * colors.length)];
                String engine = engines[(int) Math.floor(Math.random() * engines.length)];
                String transmission = transmissions[(int) Math.floor(Math.random() * transmissions.length)];
                int year = minYear + (int) Math.floor(Math.random() * (maxYear - minYear + 1));
                int VIN = max+i;
                if(!checkValid(brand, model, bodyStyle)){
                    i--;
                    continue;
                }
                //System.out.println(brand + model + bodyStyle + color + engine + transmission + year + VIN);
                cc.useCommand("INSERT INTO Vehicle (VIN, color, transmission, engine, year) VALUES (" + VIN + ", '" + color + "', '" + transmission + "', '" + engine + "', '" + year + "')");
                cc.useCommand("INSERT INTO VehicleModel (VIN, ModelName) VALUES (" + VIN + ", '" + model + "')");
                cc.useCommand("INSERT INTO VehicleBodyStyle (VIN, BodyStyle) VALUES (" + VIN + ", '" + bodyStyle + "')");
                count++;
            }

        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Something went wrong, try again later.");
        }
        return count;
    }

    public static String genBrand(CommandConstructor cc){
        String brand;
        try {
            ResultSet r = cc.useQuery("SELECT BrandName FROM BrandModels");
            ArrayList<String> list = new ArrayList<>();
            int amnt = r.getMetaData().getColumnCount();
            while(r.next()){
                for(int i = 1; i <= amnt; i++)
                    list.add(r.getObject(i).toString());
            }
            brand = list.get((int)Math.floor(Math.random() * list.size()));
        } catch (Exception e){
            return "";
        }
        return brand;
    }

    public static String genModel(CommandConstructor cc, String brand){
        String model;
        try {
            ResultSet r = cc.useQuery("SELECT ModelName FROM BrandModels WHERE BrandName='"+brand+"'");
            ArrayList<String> list = new ArrayList<>();
            while(r.next()){
                list.add(r.getObject(1).toString());
            }
            model = list.get((int)Math.floor(Math.random() * list.size()));
        } catch (Exception e){
            return "";
        }
        return model;
    }

    public static String genBodyStyle(CommandConstructor cc, String model){
        String body;
        try {
            ResultSet r = cc.useQuery("SELECT BodyStyle FROM modelBodyStyle WHERE ModelName='"+model+"'");
            ArrayList<String> list = new ArrayList<>();
            while(r.next()){
                list.add(r.getObject(1).toString());
            }
            body = list.get((int)Math.floor(Math.random() * list.size()));
        } catch (Exception e){
            return "";
        }
        return body;
    }

    public static boolean checkValid(String brand, String model, String bodyStyle){
        return (brand.length() > 0 && model.length() > 0 && bodyStyle.length() > 0);
    }

}
