## 🚀 Özellikler

- **Git Entegrasyonu**: Java uygulamasının kaynak kodunu Git servislerinden otomatik olarak çeker (ör: GitHub, GitLab).
- **Derleme Süreci**: Kod Jenkins üzerinden derlenir.
- **Konteynerleştirme**: Derlenmiş uygulama bir Docker imajına dönüştürülür.
- **İmaj Yayınlama**: Oluşturulan Docker imajı bir imaj deposuna gönderilir (ör: Docker Hub).
- **Uzaktan Dağıtım**: İmaj, farklı bir Linux sunucusunda çalıştırılır.
- **Güvenlik**: Jenkins servisine yalnızca SSL (HTTPS) üzerinden erişim sağlanır.
- **Kod ile Pipeline**: Pipeline tanımı arayüzde değil, bir Jenkinsfile olarak Git deposunda tutulur. Böylece yapılandırma kod ile versiyonlanabilir.
