#!/bin/bash

separator () {
    echo "***************************************************************************************************************************************" >> ${LOGFILE}
}

#指定されたディレクトリが無いなら作る
make_dir_if_not_exist () {
    if [ ! -e $1 ]; then
       `mkdir $1`
    fi
}

maven_install(){
    mvn install >> ${LOGFILE} 2>&1
}

PATH=/usr/bin:/bin

LOGDIR=~/install-log/

LOGFILE=${LOGDIR}`date +%Y%m%d%H%M%S`.log

make_dir_if_not_exist ${LOGDIR}

touch ${LOGFILE}

#多重起動防止機講
# 同じ名前のプロセスが起動していたら起動しない。
_lockfile="/tmp/`basename $0`.lock"
ln -s /dummy $_lockfile 2> /dev/null || { echo 'Cannot run multiple instance.'  >>${LOGFILE}; exit 9; }
trap "rm $_lockfile; exit" 1 2 3 15


# ファイル更新日時が10日を越えたログファイルを削除
PARAM_DATE_NUM=10
find ${LOGDIR} -name "*.log" -type f -mtime +${PARAM_DATE_NUM} -exec rm -f {} \;



separator
cd LoggerFactory
maven_install
separator

echo "" >> ${LOGFILE}
cd ../

separator
cd TestTool
maven_install
separator

echo "" >> ${LOGFILE}
cd ../

separator
cd ReverseLookUpMapFactory
maven_install
separator

echo "" >> ${LOGFILE}
cd ../

separator
cd LibEpg
maven_install
separator

echo "" >> ${LOGFILE}
cd ../

separator
cd ChannelListMaker
maven_install
separator

echo "" >> ${LOGFILE}
cd ../

separator
cd DumpTsPacket
maven_install
separator

echo "" >> ${LOGFILE}
cd ../

separator
cd SinglePacketDump
maven_install
separator

echo "" >> ${LOGFILE}
cd ../

separator
cd SingleSectionDump
maven_install
separator

echo "" >> ${LOGFILE}
cd ../

separator
cd DumpSDT
maven_install
separator

echo "" >> ${LOGFILE}
cd ../

separator
cd DumpEIT
maven_install
separator

echo "" >> ${LOGFILE}
cd ../

#依存ファイルをすべて組み込んだ実行可能jarを1か所に集める。既にある場合は上書きする。
DISTDIR=~/dist/

make_dir_if_not_exist ${DISTDIR}

separator
echo "ファイル移動開始" >> ${LOGFILE}

find . -type f -name "*jar-with-dependencies.jar" | xargs -t -I{} mv -f -v {} ${DISTDIR} >> ${LOGFILE} 2>&1

echo "ファイル移動完了" >> ${LOGFILE}
separator

rm $_lockfile
