package cvut.fel.felhub.impersonationservice.client;

import cvut.fel.felhub.impersonationservice.client.usermap.PeopleApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "usermapapi-service", configuration = UsermapClientConfiguration.class)
public interface UsermapClient extends PeopleApi {
}
