package de.leipzigtech.ba.config

import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.context.annotation.Configuration


@Configuration
class KotlinSecurityConfiguration : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        http {
            cors {  }
            authorizeRequests {
                authorize(AntPathRequestMatcher("/companies/**", HttpMethod.GET.name), permitAll) //hasAuthority("SCOPE_write")
                authorize(AntPathRequestMatcher("/login/**", HttpMethod.POST.name), permitAll)
                authorize(anyRequest, authenticated)
            }
            oauth2ResourceServer {
                jwt {  }
            }
        }
    }
}
