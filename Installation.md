# Introduction #

This document will be padded out to details the installation process for groovyblogs.

# Getting the Source #

Pull the latest version off the subversion repo with something like

> `hg clone https://groovyblogs.googlecode.com/hg/ groovyblogs`

# General Process #

The steps required are largely like any Grails application, but with a little custom configuration work.
  * Download the groovyblogs distro off subversion per above
  * Customise any standard settings in grails-app/conf/Config.groovy
  * (Optionally) Provide any custom override settings in /opt/groovyblogs/groovyblogs-config.properties
  * Do a "grails war" to generate the war file for deployment to your app server

# App Server Configuration #

The app assumes you have a JNDI datasource configured under the name "jdbc/groovyblogs". Consult your app server manuals for details on setting up JNDI data sources.

In order for the JFreechart to be happy, you'll also need to define -Djava.awt.headless=true to the jvm.

You may need to alter server memory config on your appserver - See examples on the Discoblog (http://thediscoblog.com/2009/08/18/increasing-tomcats-memory/)

# Thumbnail Support #

groovyblogs uses bluga.net (http://webthumb.bluga.net/) for thumbnail generation. If you want thumbnails on your site, you can signup for a free account to get 100 thumbs/month for nix.

# Running Memcache #

If you turn thumbnails on, you'll need to be running memcached to host the cached copies. The standard configuration in /grails-app/conf/spring/resources.groovy assumes the default memcache port (11211) is used. More detailed discussion can be found on my blog (http://blogs.bytecode.com.au:80/glen/2009/08/18/a-first-taste-of-memcache.html).

# What sort of stuff goes in the custom properties file? #

Pretty much anything you want to customize (and don't want to commit to your scm). Perhaps something like this:

```
grails.mail.host=mail.host.com
grails.mail.default.from=yourname@yourhost.com

thumbnail.user=1234

thumbnail.apiKey=1122334411223344
translate.apikey=ASDFasdfASFDasdfASDFasdf

feeds.moderator_email=moderator@host.com
feeds.moderate=true

twitter.enabled=true
twitter.user = your_twitter_id
twitter.password = pa$$word

http.useproxy=true
http.host=your.proxy.host.com
http.port=3128
```