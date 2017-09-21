import com.predic8.membrane.core.util.URLParamUtil;
import org.junit.Test;

import java.util.Map;

/**
 * Created by Administrator on 9/21/2017.
 */
public class UrlParamUtilsTest {

    @Test
    public void parseTest(){
        Map<String, String> params = URLParamUtil.parseQueryString("a=1&b=2&c=3&b=4");
        System.out.println(params);
    }
}
