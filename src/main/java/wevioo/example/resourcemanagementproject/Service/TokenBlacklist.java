package wevioo.example.resourcemanagementproject.Service;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class TokenBlacklist {

    private final Set<String> blacklistedTokens =
            Collections.synchronizedSet(new HashSet<>());

    public void add(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

}
