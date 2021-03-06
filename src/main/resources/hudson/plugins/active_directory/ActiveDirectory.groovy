/*
    Configure Active Directory as the authentication realm.
*/
import org.acegisecurity.providers.ProviderManager
import org.acegisecurity.providers.anonymous.AnonymousAuthenticationProvider
import org.acegisecurity.providers.rememberme.RememberMeAuthenticationProvider
import hudson.plugins.active_directory.ActiveDirectoryAuthenticationProvider
import hudson.plugins.active_directory.ActiveDirectoryUnixAuthenticationProvider
import jenkins.model.Jenkins

// global so that this bean can be retrieved as UserDetailsService
if (realm.domains == null && realm.descriptor.canDoNativeAuth())
    // Windows path requires com4j, which is currently only supported on Win32
    activeDirectory(ActiveDirectoryAuthenticationProvider, realm)
else
    activeDirectory(ActiveDirectoryUnixAuthenticationProvider,realm) {}

authenticationManager(ProviderManager) {
    providers = [
        activeDirectory,

    // these providers apply everywhere
        bean(RememberMeAuthenticationProvider) {
            key = Jenkins.getInstance().getSecretKey()
        },
        // this doesn't mean we allow anonymous access.
        // we just authenticate anonymous users as such,
        // so that later authorization can reject them if so configured
        bean(AnonymousAuthenticationProvider) {
            key = "anonymous"
        }
    ]
}
