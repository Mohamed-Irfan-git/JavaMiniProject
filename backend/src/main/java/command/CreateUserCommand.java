package command;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.requestDto.UserRequestDTO;
import service.UserService;

public class CreateUserCommand implements Command {

    private final UserService userService;
    private final ObjectMapper mapper = new ObjectMapper();

    public CreateUserCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(Object data, ClientContext context) {

        try {
            UserRequestDTO request = mapper.convertValue(data, UserRequestDTO.class);
            System.out.println("DTO role: " + request.getRole());

            boolean success = userService.createUser(request);

            if (success) {
                context.getOutput().println(
                        "{\"success\":true,\"message\":\"User created successfully\"}"
                );
            } else {
                context.getOutput().println(
                        "{\"success\":false,\"message\":\"User creation failed\"}"
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
            context.getOutput().println(
                    "{\"success\":false,\"message\":\"Server error\"}"
            );
        }
    }
}