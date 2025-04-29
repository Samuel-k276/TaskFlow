FROM node:20-alpine as build

WORKDIR /app

# Copie os arquivos de configuração do projeto
COPY package*.json ./

# Instale as dependências
RUN npm install

# Copie o código-fonte
COPY . .

# Substitua a configuração de ambiente para usar caminhos relativos
RUN mkdir -p src/app/config
RUN echo "export const environment = { \
    production: true, \
    apiUrl: '/api', \
    backendUrl: '', \
    frontendUrl: '' \
};" > src/app/config/environment.ts

# Compile o aplicativo para produção
RUN npm run build -- --configuration production

# Segunda etapa: imagem de execução
FROM nginx:stable-alpine

# Copie os arquivos compilados da etapa de build
COPY --from=build /app/dist/task-app-frontend/browser/ /usr/share/nginx/html

# Copie a configuração do Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponha a porta 80
EXPOSE 80

# Inicie o Nginx
CMD ["nginx", "-g", "daemon off;"]