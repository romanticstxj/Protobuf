import com.google.protobuf.GeneratedMessage;
import com.madhouse.protobuf.DataFileReader;
import com.madhouse.protobuf.DataFileWriter;
import com.madhouse.rtb.PremiumMadRTBProtocol;
import com.sun.xml.internal.bind.marshaller.DataWriter;

import java.io.File;

/**
 * Created by WUJUNFENG on 2017/6/12.
 */
public class Main {
    public static void main(String[] args) {
        PremiumMadRTBProtocol.BidRequest.Builder builder = PremiumMadRTBProtocol.BidRequest.newBuilder();
        builder.setId("madhouse");
        builder.setTest(0);
        builder.setTmax(100);

        DataFileWriter<PremiumMadRTBProtocol.BidRequest> dataFileWriter = new DataFileWriter<PremiumMadRTBProtocol.BidRequest>();
        dataFileWriter.create(new File("c:\\1.data"));

        for (int i = 0; i < 100; ++i) {
            dataFileWriter.write(builder.build());
        }

        dataFileWriter.close();

        int count = 0;
        DataFileReader<PremiumMadRTBProtocol.BidRequest> dataFileReader = new DataFileReader<PremiumMadRTBProtocol.BidRequest>(PremiumMadRTBProtocol.BidRequest.class);
        dataFileReader.open(new File("c:\\1.data"));
        while (dataFileReader.hasNext()) {
            try {
                PremiumMadRTBProtocol.BidRequest bidRequest = dataFileReader.next();
                count += 1;
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        }
    }
}
