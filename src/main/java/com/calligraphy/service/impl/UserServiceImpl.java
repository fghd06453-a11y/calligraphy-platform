private final UserMapper userMapper;
private final JwtUtil jwtUtil;

public UserServiceImpl(UserMapper userMapper, JwtUtil jwtUtil) {
    this.userMapper = userMapper;
    this.jwtUtil = jwtUtil;
}