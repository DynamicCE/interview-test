# Interview Test Application

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Bu proje, yazılım geliştiriciler için mülakat sorularını otomatik olarak oluşturan ve yöneten bir uygulamadır. OpenAI ve HuggingFace entegrasyonları ile farklı kategorilerde sorular üretebilir.

## Özellikler

- Core Java, Spring Framework, SQL ve Design Patterns kategorilerinde soru üretimi
- OpenAI GPT-3.5 Turbo entegrasyonu
- HuggingFace CodeLlama-7b entegrasyonu
- PostgreSQL veritabanı desteği
- Modern ve kullanıcı dostu arayüz

## Teknolojiler

### Backend

- Java 17
- Spring Boot 3.2
- Spring Security
- Spring Data JPA
- PostgreSQL
- Lombok

### Frontend

- React + TypeScript
- Vite
- TailwindCSS
- Shadcn/ui

## Kurulum

1. Repository'yi klonlayın

```bash
git clone https://github.com/yourusername/interview-test.git
```

2. Backend için application.properties dosyasını oluşturun

```bash
cd interview-test-backend/src/main/resources
cp application.properties.example application.properties
```

3. Frontend bağımlılıklarını yükleyin

```bash
cd interview-test-frontend
npm install
```

4. Uygulamayı çalıştırın

```bash
# Backend
./mvnw spring-boot:run

# Frontend
npm run dev
```

## Lisans

MIT
