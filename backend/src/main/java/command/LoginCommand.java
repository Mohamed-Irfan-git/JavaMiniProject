package command;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.requestDto.LoginRequestDTO;

public class LoginCommand implements Command {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void execute(Object data, ClientContext context) {


        LoginRequestDTO login =
                mapper.convertValue(data, LoginRequestDTO.class);

        String username = login.getUsername();
        String password = login.getPassword();

        // TODO: call service layer

        context.getOutput().println(
                "{\"status\":\"success\",\"message\":\"Login successful\"}"
        );
    }
}