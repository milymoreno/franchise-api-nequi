#!/bin/bash
set -e

# Configuración (puedes ajustar el profile si usas otro)
AWS_PROFILE="mily-aws-student"
AWS_REGION="us-east-1"
APP_NAME="franchise-api"
ENV_NAME="franchise-api-production"

echo "==================================================="
echo "🚀 Iniciando proceso de despliegue a AWS Beanstalk"
echo "==================================================="

echo "1. Compilando la aplicación (JAR)..."
./mvnw package -DskipTests

echo "2. Preparando paquete ZIP..."
# IMPORTANTE: Beanstalk espera exactamente 'Dockerfile' para AL2
cp Dockerfile.eb Dockerfile
zip -j deploy.zip Dockerfile target/franchise-api-0.0.1-SNAPSHOT.jar

# Obteniendo el bucket dinámico de Terraform
cd terraform
S3_BUCKET=$(terraform output -raw s3_bucket)
cd ..

if [ -z "$S3_BUCKET" ]; then
    echo "❌ Error: No se pudo obtener el nombre del bucket S3 de Terraform."
    echo "Asegúrate de que 'terraform apply' se haya ejecutado."
    exit 1
fi

VERSION_LABEL="v-$(date +%Y%m%d%H%M%S)"

echo "3. Subiendo a S3 ($S3_BUCKET)..."
aws s3 cp deploy.zip "s3://$S3_BUCKET/deploy.zip" --profile "$AWS_PROFILE" --region "$AWS_REGION"

echo "4. Creando nueva versión de la aplicación ($VERSION_LABEL)..."
aws elasticbeanstalk create-application-version \
    --application-name "$APP_NAME" \
    --version-label "$VERSION_LABEL" \
    --source-bundle S3Bucket="$S3_BUCKET",S3Key=deploy.zip \
    --profile "$AWS_PROFILE" \
    --region "$AWS_REGION" \
    --no-cli-pager

echo "5. Desplegando versión en el entorno ($ENV_NAME)..."
aws elasticbeanstalk update-environment \
    --environment-name "$ENV_NAME" \
    --version-label "$VERSION_LABEL" \
    --profile "$AWS_PROFILE" \
    --region "$AWS_REGION" \
    --no-cli-pager

echo "==================================================="
echo "✅ Despliegue iniciado correctamente."
echo "La versión $VERSION_LABEL se está instalando."
echo "Puedes revisar el progreso en la consola de AWS Elastic Beanstalk."
echo "URL de Swagger esperada: http://$ENV_NAME.../swagger-ui.html"
echo "==================================================="
