import fr.i360matt.citronade.Citronade;
import fr.i360matt.citronade.operations.get.ResultOption;

import java.util.List;
import java.util.Map;

public class Tests {

    public static void main (String[] args) {


        Citronade citronade = new Citronade((auth -> {
            auth.setHost("127.0.0.1");
            auth.setPort(3307);
            auth.setUser("root");
            auth.setPassword(null);
            auth.setDatabase("citronade");
        }));

        citronade.insert("table", (map) -> {
            map.put("id", 1);
            map.put("name", "test");
        });

        citronade.update("table", (query, update) -> {
            query.put("id", 1);
            update.put("name", "test2");
        });





        ResultOption resultOption = citronade.get("table", (query) -> {
            query.put("id", 1);
        });
        Map<String, Object> map = resultOption.getOneRaw();
        List<Map<String, Object>> list = resultOption.getAllRaw();

        Obj obj = resultOption.getOne(Obj.class);
        List<Obj> listObj = resultOption.getAll(Obj.class);


        resultOption.getOneRaw((values) -> {

        });
        resultOption.getAllRaw((listOfValues) -> {

        });
        resultOption.getOne(Obj.class, (obj) -> {

        });
        resultOption.getAll(Obj.class, (listOfObj) -> {

        });

        // So:
        citronade.get("table", (query) -> {
            query.put("id", 1);
        }).getOne(Obj.class, (obj) -> {

        });





        citronade.delete("table", (query) -> {
            query.put("id", 1);
        });


    }

}
