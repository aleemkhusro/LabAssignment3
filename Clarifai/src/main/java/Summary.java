import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import okhttp3.OkHttpClient;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.typography.hershey.HersheyFont;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Naga on 24-01-2017.
 */
public class Summary {
    public static void main(String[] args) throws IOException {
        final ClarifaiClient client = new ClarifaiBuilder("KKQIegBW9uOl_3vaMSzqq4QCfPNyNBvB7XNBz1vE", "xsY48eiDhhsFo5M7HE3F71ZYkB_tEQmemlWekTgG")
                .client(new OkHttpClient()) // OPTIONAL. Allows customization of OkHttp by the user
                .buildSync(); // or use .build() to get a Future<ClarifaiClient>
        client.getToken();

        File file = new File("output/mainframes");
        File[] files = file.listFiles();
        //ArrayList<String> words = new ArrayList ();
        HashMap<String, Integer > hm = new HashMap<String, Integer>();
        for (int i=0; i<files.length;i++){
            ClarifaiResponse response = client.getDefaultModels().generalModel().predict()
                    .withInputs(
                            ClarifaiInput.forImage(ClarifaiImage.of(files[i]))
                    )
                    .executeSync();
            List<ClarifaiOutput<Concept>> predictions = (List<ClarifaiOutput<Concept>>) response.get();
            MBFImage image = ImageUtilities.readMBF(files[i]);
            //int x = image.getWidth();
            //int y = image.getHeight();


            System.out.println("*************" + files[i] + "***********");
            List<Concept> data = predictions.get(0).data();
            System.out.println(data.get(0).name());


            for (int j = 0; j < data.size(); j++) {
               System.out.println(data.get(j).name() + " - " + data.get(j).value());
              // words.add(data.get(j).name());
               if(!hm.containsKey(data.get(j).name()))
                   hm.put(data.get(j).name(), 1);
               else
                   hm.put(data.get(j).name(), hm.get(data.get(j).name())+1);




               // image.drawText(data.get(j).name(), (int)Math.floor(Math.random()*x), (int) Math.floor(Math.random()*y), HersheyFont.ASTROLOGY, 20, RGBColour.RED);
            }
            //DisplayUtilities.displayName(image, "image" + i);
        }
        for(Map.Entry<String, Integer> entry: hm.entrySet()){
            System.out.println("Word: "+entry.getKey()+ " Frequency: " +entry.getValue() );
        }


    }
}
