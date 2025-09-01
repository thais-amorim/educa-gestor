# Dockerfile otimizado para Quarkus
FROM registry.access.redhat.com/ubi9/openjdk-21:1.23

ENV LANGUAGE='en_US:en'

# Cria diretório de trabalho
WORKDIR /deployments

# Copia as dependências primeiro (para melhor cache do Docker)
COPY --chown=185 build/quarkus-app/lib/ ./lib/
COPY --chown=185 build/quarkus-app/*.jar ./
COPY --chown=185 build/quarkus-app/app/ ./app/
COPY --chown=185 build/quarkus-app/quarkus/ ./quarkus/

# Expõe a porta da aplicação
EXPOSE 8080

# Configura o usuário não-root
USER 185

# Variáveis de ambiente para o Quarkus
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

# Comando de entrada
ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]
