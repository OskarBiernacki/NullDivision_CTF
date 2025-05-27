FROM alpine:latest

# Instalacja vsftpd (lekki serwer FTP) i Pythona
RUN apk add --no-cache vsftpd python3

# Utworzenie katalogu dla FTP i przykładowego pliku
RUN mkdir -p /ftp && \
    echo "Witaj na serwerze FTP!" > /ftp/readme.txt

# Konfiguracja vsftpd
RUN echo "listen=YES\n\
anonymous_enable=YES\n\
local_enable=NO\n\
write_enable=NO\n\
anon_root=/ftp\n\
anon_upload_enable=NO\n\
anon_mkdir_write_enable=NO\n\
anon_other_write_enable=NO\n\
no_anon_password=YES\n\
listen_ipv6=NO\n\
pasv_enable=NO\n\
ftpd_banner=FTP only on port 21.\n\
" > /etc/vsftpd.conf

# Otwórz porty FTP
EXPOSE 21

# Domyślny katalog roboczy
WORKDIR /ftp

# Uruchomienie vsftpd i możliwość uruchamiania skryptów Pythona
CMD vsftpd /etc/vsftpd.conf