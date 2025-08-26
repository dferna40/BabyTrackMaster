package config;

public interface UserIdResolver {

	Long resolve(String username);
}
