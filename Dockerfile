FROM fnndsc/ubuntu-python3

# Aktualizacja pakietów i instalacja wymaganych narzędzi
RUN python3 -V 
RUN apt-get update && apt-get install -y vsftpd

# Utworzenie katalogu dla FTP i przykładowego pliku
RUN mkdir -p /ftp && \
    echo "Witaj na serwerze FTP!" > /ftp/readme.txt
RUN chmod 755 /ftp && \
    chmod 644 /ftp/readme.txt

# Utworzenie katalogu secure_chroot_dir dla vsftpd
RUN mkdir -p /var/run/vsftpd/empty

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
pasv_enable=YES\n\
pasv_min_port=21000\n\
pasv_max_port=21010\n\
secure_chroot_dir=/var/run/vsftpd/empty\n\
ftpd_banner=Witaj na serwerze FTP.\n\
" > /etc/vsftpd.conf

# Otwórz port FTP i zakres portów dla trybu pasywnego
EXPOSE 2137 21 21000-21010

# Domyślny katalog roboczy
WORKDIR /ftp
COPY nulldivision-client.apk ./nulldivision-client.apk
RUN chmod 644 nulldivision-client.apk

RUN mkdir /server_nd && chmod 755 /server_nd
WORKDIR /server_nd
COPY server.py ./server.py
COPY requirements.txt ./requirements.txt

RUN pip install --no-cache-dir -r requirements.txt

ENTRYPOINT []

# Skrypt startowy dla serwera
RUN echo IyEvYmluL2Jhc2gKdnNmdHBkIC9ldGMvdnNmdHBkLmNvbmYgJgpweXRob24zIC9zZXJ2ZXJfbmQvc2VydmVyLnB5 | base64 -d > /server_nd/start.sh
RUN chmod +x /server_nd/start.sh
CMD ["/server_nd/start.sh"]