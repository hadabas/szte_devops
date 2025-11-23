import jenkins.model.*
import hudson.security.*

def instance = Jenkins.getInstance()

println "--> Creating default admin user"

def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount("admin","admin") // felhasználó/jelszó
instance.setSecurityRealm(hudsonRealm)

def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
instance.setAuthorizationStrategy(strategy)

instance.save()
println "--> Admin user created: admin/admin"