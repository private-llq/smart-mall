package com.jsy.basic.util.utils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.jsy.basic.util.vo.UserDto;
import java.util.Date;
public class JwtUtils {
    //一万天
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 10000000L;

    private static final String TOKEN_SECRET = "p7rOWePP2FAfaIPN";

    private static final String ISSUER = "jsy.com@2020";

    /**
     * 登录的时候把用户信息放到Token中
     * @param userDto
     * @return
     */
    public static String createUserToken(UserDto userDto) {
        Date now = new Date();
        //算法
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);

        String token = JWT.create()
                //签发人
                .withIssuer(ISSUER)
                //签发时间
                .withIssuedAt(now)
                //过期时间
                .withExpiresAt(new Date(now.getTime() + EXPIRE_TIME))
                //保存身份标识
                .withClaim("uuid", userDto.getUuid())
                .withClaim("name", userDto.getName())
                .withClaim("phone", userDto.getPhone())
                .withClaim("userType", userDto.getUserType())
                .withClaim("relationUuid", userDto.getRelationUuid())
                .withClaim("head_sculpture",userDto.getHeadSculpture())
                .sign(algorithm);
        return token;
    }

    /**
     * 校验token
     * @param token
     * @return
     */
    public static boolean checkToken(String token) {
        try {
            //算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 解密token 获取user对象
     * @param jwtToken
     * @return
     * @throws Exception
     */
    public static UserDto decodeToken(String jwtToken) throws Exception {
        UserDto userDto;
        try {
            userDto=new UserDto();
            userDto.setUuid(JWT.decode(jwtToken).getClaim("uuid").asString());
            userDto.setName(JWT.decode(jwtToken).getClaim("name").asString());
            userDto.setPhone(JWT.decode(jwtToken).getClaim("phone").asString());
            userDto.setUserType(JWT.decode(jwtToken).getClaim("userType").asInt());
            userDto.setRelationUuid(JWT.decode(jwtToken).getClaim("relationUuid").asString());
            userDto.setHeadSculpture(JWT.decode(jwtToken).getClaim("head_sculpture").asString());
            return userDto;
        } catch (Exception ex) {
            System.out.println("token解析失败");
        }
        return  null;
    }

    public static void main(String[] args) throws Exception {
        UserDto userDto = decodeToken("gxZjVkZTA3NGQ4NDk3ZDE2ODU3YWFhNWQ4M2YifQ.cDHGgnwUfHlBq0p02NQKVS4pPTE2o7sua75NuQgivY4");
        System.out.println("userDto = " + userDto);
    }
}
