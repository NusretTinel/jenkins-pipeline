## Özgür Debian  VM ve Jenkins Pipeline 
 

- **Araştırma**
- **Sunucuların Kurulumu**
- **Sunuculara Gerekli Paketlerin Kurulması**
- **Ssl Kurulumu**
 **Güvenlik Katmanı**
- **Jenkins Kurulumu**
- **Docker Kurulumu**
- **Github Reposu ve Gerekli Bağlantılar**
---
### Araştırma :

 + **Rocky 9, Debian 12 ya da Ubuntu LTS Sunucu 22.04 arasında karşılaştırma ve araştırma yapıldı .**

 + **Debian 12 daha az hata verdiği ve daha stabil olduğu düşünüldüğü için seçildi.**
---
 
### **Sunucuların Kurulumu**


 ### VM1: Jenkins Sunucusu Kurulumu:
1. **Debian 12 İndirme**
- https://www.debian.org/distrib/ Üzerinden Debian 12 netinst Iso dosyasını indir. (Minimal bir kurulum için netinst indiriyoruz).
2. **Sanal Makine Kurulumu**
+ Virtual Box İndir(Kullanımı kolay ve ücretsiz olduğu için Virtual Box tercih ettim)
+ Virtual Box çalıştır.
+ Yeni(new) butonuna bas  
+ İsim olarak jenkins-server ver (İsim ayırt edici bir isim değil fakat diğer yerlerde de aynı ismi kullanacağız.) 
+ Tür olarak Linux , Versiyon olarak Debian seç . 
+ Ram olarak 4 gb tercih edilebilir.
+ Disk "Create VİRTUAL HARD DİSK NOW " seçeneğini seç ve 20 gb disk oluştur.
+ Tamam de . 
+ Vm ayarlarına gir . -> storage > controller : IDE > ISO dosyasını seç (Önceki adımlarda indirdiğimiz.)
+ Network > Adapter 1 > "Bridge Adapter " seçeneğini seç.(Ağ erişimi için gerekli).
3. **Debian 12 Kurulumu**
+ Hazırladığımız VM'i başlat . 
+ İnstall seçeneğini seç (Grafik Paketini kurmuyoruz.)
+ Dil English olarak tercih edilebilir.
+ Konum Türkiye 
+ Klavye  (Tercihine göre Turkish Q seçebilirsin).
+ Hostname: jenkins-server 
+ Domain : Boş bırak 
+ Root şifresi : Güçlü ve bir şifre gir .
+ Kullanıcı oluşturucaz. 
+ Tam isim : "Jenkins Admin " veya istediğin bir isim.
+ Kullanıcı adı : jenkins-admin
+ Şifre: Güçlü bir şifre gir. 
+ Disk bölümleme : "Guided - Use entire " disk seç.
+ LVM'siz tek bölümleme yap. 
+ Yes ile onayla
+ Paket kaynağı : Türkiye seçebilirsin.
+ Paket seçimi : "SSH server " ve "standard system utilities " seç.(Minimal kurulum istediğimiz için bu iki paket yeterli.)
+ GRUB'u diske kur .
+ Yes de ve /dev/sda seç.
+ Continue de ve Vm yeniden başlayacak.

### **Sunuculara Gerekli Paketlerin Kurulumu**

**1. Sudo kurulumu** 
+ Kurmak için root yetkisine sahip olmamız gerekiyor.
+ Root moduna geçmek için su - -> şifre gir .
+ Şimdi sudo kurmak için apt update && apt install sudo -y komutunu çalıştır . (Apt paket yöneticisiyle sudo kuruyoruz)
+ Normal kullanıcıda kullanıcağımız için kullanıcıya sudo yetkisi veriyoruz. 
+ usermod -aG sudo <kullanıcı-adı> (kullanıcı adı kısmına kullanıcı oluştururken girdiğimiz ismi giriyoruz.)
+ Sonrasında su - <kullanıcı-adı> yaparak kullanıcıya geçiş yapıyoruz.
+ reboot ile sistemi yeniden başlatıyoruz.
+ sudo whoami komutuyla yetkiyi kontrol edebilirsin.

**2. Curl kurulumu (İnternetten dosya indirmek için curl kuruyoruz.)**
+ sudo apt update ( Paket listesini güncelliyoruz.)
+ sudo apt install -y curl komutlarını sırasıyla çalıştır. (-y onay istememesini sağlıyor.)

**3. SSH bağlantısı**
+ Vm ile beraber rahat çalışabilmek kopyala yapıştır vb işlemleri rahatça yapabilmek için ssh bağlantısı tercih edebilirsin.
+ Vm üzerinde ip a komutunu çalıştırdığımızda Vm Ip'sine ulaşıyoruz.
+ ssh jenkins-admin@<ip> komutu ile bağlanıyoruz. 
+ Kullanıcı adı ve şifre ile giriş yapabilirsin.

**4. OpenJDK 17 kurulumu**
+ sudo apt install -y openjdk-17-jdk (Bu komut ile java 17 jdk sını kuruyoruz.)
+ java -version olarak kurulumu kontrol ediyoruz.

--- 

## **Docker kurulumu**
+ sudo apt install -y docker.io  ( komutu ile docker kuruyoruz. )
+ sudo usermod -aG docker jenkins-admin (Kullanıcıyı docker grubuna ekliyoruz.)
+ jenkins-admin'in sudo olmadan Docker komutlarını çalıştırmasını sağlıyoruz. Jenkins bu kullanıcı ile çalışacak bu yüzden yapıyoruz.
+ sudo systemctl enable docker (systemctl Enable sistem çalıştığında otomatik başlatmayı sağlayacak)
+ sudo systemctl start docker (start dockerı başlatır. )
+ docker --version ile kurulumu kontrol ediyoruz.

--- 
## **Jenkins Kurulumu**
+ İndiriceğimiz jenkins deposunu doğrulamak için resmi anahtar gerekli 
+ curl -fsSL https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key | sudo tee /usr/share/keyrings/jenkins-keyring.asc > /dev/null 
+ Yukarıdaki komutu çalıştırıp anahtarı indirip kaydediyoruz.
+ echo "deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] https://pkg.jenkins.io/debian-stable binary/" | sudo tee /etc/apt/sources.list.d/jenkins.list 
+ Jenkins deposunu paket kaynaklarına ekliyoruz.Apt ile kurmak için yapıyoruz.
+ cat /etc/apt/sources.list.d/jenkins.list (Depo dosyasını kontrol ediyoruz.Deb signed ile başlayan bir çıktı olması gerekiyor.)
+ sudo apt update (Paketleri güncelliyoruz)
+ sudo apt install -y jenkins (Jenkins kuruyoruz)
+ sudo systemctl enable jenkins (Açılışta başlamasını sağlıyoruz.)
+ sudo systemctl start jenkins (Başlatıyoruz)
+ sudo cat /var/lib/jenkins/secrets/initialAdminPassword (Jenkinsin ilk giriş için ürettiği şifreyi alıyoruz.Arayüz girişi için gerekli)
---  
**Jenkinsi güvenli hale getirmek ve SSL için ngnix kuracağız**.
+ sudo apt install -y nginx (Komutu ile nginx'i kuruyoruz).
+ sudo mkdir /etc/nginx/ssl (ssl sertifikalarını ve anahtarlarını saklamak için klasör oluşturuyoruz. )
+ sudo openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout /etc/nginx/ssl/jenkins.key -out /etc/nginx/ssl/jenkins.crt (Self signed bir SSL sertifikası ve anahtar oluşturuyoruz.)
+ Karşımıza sorular gelecek. 
**Country**: Tr 
**State**: Istanbul 
**Organization**: ÖzgürYazılım(istenilen isim girilebilir.) 
**Common name**: jenkins-server
 **Nginx'i yapılandıracağız.**
+ sudo nano /etc/nginx/sites-available/jenkins komutunu çalıştır.
+ Karşına bir dosya açılacak.Buraya aşşağıdaki içeriği ekleyeceğiz.
server {
    listen 80;
    server_name jenkins-server;
    return 301 https://$host$request_uri;
}

server {
    listen 443 ssl;
    server_name jenkins-server;

    ssl_certificate /etc/nginx/ssl/jenkins.crt;
    ssl_certificate_key /etc/nginx/ssl/jenkins.key;

    location / {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
+ Ekledikten sonra ctrl+x ile kaydedip çıkıyoruz.
+ HTTP'yi HTPPS'e yönlendirir ve Jenkins'e SSL ile proxy yapar. Güvenli erişim için gerekli.
+ sudo ln -s /etc/nginx/sites-available/jenkins /etc/nginx/sites-enabled/
+ sudo systemctl restart nginx ( Nginx'i aktifleştirip yeniden başlatıyoruz.)
+ Jenkinsi Yerel Ip'ye bağlayacağız. 
+ sudo nano /etc/default/jenkins (nano ile dosyayı açıyoruz.)
+ Aşşağıdaki gibi başlayan satırı aşşağıdaki ile değiştiriyoruz.
+ JENKINS_ARGS="--httpListenAddress=127.0.0.1 --httpPort=8080"
+ Bu Jenkins'i yerel IP'ye bağlamamızı sağlıyor.(Sadece localhost üzerinden erişilebilir hale getiriyoruz. Doğrudan erişimi engelleyip sadece nginx üzerinden gidiyoruz.)
+ sudo systemctl restart jenkins 
---
**Jenkins'i ilk kez yapılandırıyoruz**
+ Tarayıcıda https://<ip adresi> adresine gidiyoruz.
+ Önceki adımlarda aldığımız ilk şifreyi burada kullanıyoruz.
+ Install suggested plugins seçeneğini seçiyoruz.
+ Git - Pipeline - Docker pipeline- SSH Agent Plugin eklentilerini kuruyoruz.
+ Yönetici hesabı oluştur. kullanıcı: admin  , şifre : istediğin bir şifre . 
---
## **VM2 : Dağıtım Sunucusu Kurulumu ** 
+ Vm1 kurulum adımlarındaki gibi Debian12 kuruyoruz.
+ Bu sefer isim olarak farklı bir isim kullan ben app-server kullandım.
+ Güçlü bir şifre belirle.
---
**Docker Kurulumu**
sudo apt update
sudo apt install -y docker.io
sudo usermod -aG docker jenkins-admin
sudo systemctl enable docker
sudo systemctl start docker
+ Yukarıdaki komutları çalıştırıyoruz(Docker kurup dockerı kullanıcı grubuna ekleyip sistemi başlatıyor.Sistem çalıştığında otomatik başlayacak.)
+ SSH erişimi ayarlayacağız.
+ jenkins-server üzerinde 
 ssh-keygen -t rsa -b 4096 -f ~/.ssh/id_rsa
 ssh-copy-id jenkins-admin@<app serverin ip si >
+ Komutlarını çalıştırıyoruz. Bu komutlar bir ssh anahtarı üretip bunu diğer servera kopyalıyor. Bu da parolasız ssh erişimi sağlıyor.
+ Bu adımı yapmamızın sebebi pipeline uygulamayı dağıtmak için ssh kullanacak.
---
**Jenkins Pipeline Kurulumu** 
**1. Pipeline için Git Deposu Oluşturma**
+ Github'da jenkins-pipeline adında bir depo oluştur.
**İçeriği:**
pipeline {
    agent any
    stages {
        stage('Kod Çekme') {
            steps {
                git url: 'https://github.com/<kullanıcı>/jenkins-pipeline.git', branch: 'main'
            }
        }
        stage('Derleme') {
            steps {
                sh 'javac Main.java'
                sh 'jar cvf app.jar *.class'
            }
        }
        stage('Docker İmajı Oluşturma') {
            steps {
                script {
                    def appImage = docker.build("<kullanıcı>/app:${env.BUILD_NUMBER}")
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials') {
                        appImage.push()
                    }
                }
            }
        }
    stage('Dağıtım') {
    steps {
        sshagent(['app-server-ssh']) {
            sh 'bash -c "ssh jenkins-admin@192.168.1.16 docker stop app || true"'
            sh 'bash -c "ssh jenkins-admin@192.168.1.16 docker rm app || true"'
            sh "bash -c \"ssh jenkins-admin@192.168.1.16 docker run -d --name app -p 8081:8080 <kullanıcı>/app:${env.BUILD_NUMBER}\""
        }
    }
}
    }
}

+ Kullanıcı yazan yerleri kendi Docker Hub/GitHub bilgilerinizle değiştiriniz.
+ Pipeline'ı Git'te tutarak sürüm kontrolü sağlıyoruz.
---
**2. Java uygulama deposu oluşturma**
+ Github'da java-app adında bir repo oluştur.
+ Main.java dosyası ekle.
**İçeriği:**

public class Main {
    public static void main(String[] args) {
        System.out.println("Jenkins’tan Merhaba!");
    }
}

---
**3. Bir DockerFile ekle**
**İçeriği**
FROM openjdk:17-jdk-slim
COPY app.jar /app.jar
CMD ["java", "-jar", "/app.jar"]

---
**4. Jenkins'te Kimlik Bilgileri Ayarlama**
 Jenkins arayüzüne git> Manage Jenkins -> Manage Credentials.
**4.1 Docker Hub**
Kind: Usarname with password
ID: dockerhub-credentials
Username: Docker hub kullanıcı adın
Password: Docker hub şifren veya tokenin

**4.2 SSH**
+ Kind: SSH usarname ve private key 
+ ID : app-server-ssh 
+ Username: jenkins-admin
+ Private key: jenkins serverdaki  ~/.ssh/id_rsa içeriğini kopyala
Burada pipeline app servera bağlanmak ve docker huba yüklemek için bu bilgilere ihtiyaç duyuyor.
---
**5 Pipeline Oluşturma**
+ Jenkinste "New Item " 
+ İsim : java-app-pipeline
+ Tür : Pipeline 
+ Tamama basın ve devam edin.
**5.1 Jenkinsfile Bağlama**
+ Yapılandırma sayfasına git.
+ Pipeline sekmesine git.
+ Definition: Pipeline script from SCM seç
+ SCM : Git
+ Repository URL: https://github.com/<kullanici>/jenkins-pipeline.git
+ Branch: */main
+ Script Path : Jenkinsfile
+ Save tuşuna bas ve devam et.
Bu adım sayesinde pipeline git üzerinden kontrol edilecek.
**5.2 Pipeline Çalıştırma**
+ Pipeline sayfasında -> build now seçeneğine veya şimdi yapılandır seçeneğine tıkla
+ Eğer yeşil bir tik görüyorsan pipeline başarılı.
+ Eğer kırmızı bir çarpı görüyorsan pipeline başarısız olmuş demektir . 
+ Console output veya overview diyerek hatayı ve hangi aşamada olduğunu görebilirsin.

**Son**
Sistem başarılı bir şekilde çalışıyor olması gerekmekte.Tekrar açıldığında tüm sistemler otomatik başlayacaktır. Java kodu başlayıp biteceği için aktif olarak çalışıyor görünmeyebilir. Çıktıyı **docker logs <konteyner adı>** olarak görebilirsiniz. 