package deltix.util.memory;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;


/**
 * Created by Alex Karpovich on 06/10/2020.
 */
@Tag("utils")
public class Test_MDI {

    @Test
    public void testUTF8() {
        byte[] bytes = new byte[] {0, 64, 68, 111, 119, 110, 108, 111, 97, 100, 32, 102, 114, 101, 101, 32, 77, 117, 115, 101, 111, 32, 55, 48, 48, 32, 102,
                111, 110, 116, 32, -16, -97, -110, -109, 32, 100, 97, 102, 111, 110, 116, 102, 114, 101, 101, 46, 110, 101, 116, 32, 45, 32, 71, 111, 111, 103, 108, 101, 32, 67,
                104, 114, 111, 109, 101};

        System.out.print("UTF8 from .NET = ");
        System.out.println(new String(bytes, 2, bytes.length - 2,  StandardCharsets.UTF_8));

        MemoryDataInput input = new MemoryDataInput();
        input.setBytes(bytes);
        String decoded = input.readString();
        System.out.print("UTF8 from JAVA = ");
        System.out.println(decoded);

    }
}
