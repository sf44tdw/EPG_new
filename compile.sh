#!/bin/bash

separator () {
    echo "***************************************************************************************************************************************" >> ${LOGFILE}
}

maven_install(){
mvn install >> ${LOGFILE} 2>&1
}

PATH=/usr/bin:/bin

LOGDIR=~/install-log/

LOGFILE=${LOGDIR}`date +%Y%m%d%H%M%S`.log

if [ ! -e ${LOGDIR} ]; then
`mkdir ${LOGDIR}`
fi

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
# ここまではできた

rm $_lockfile