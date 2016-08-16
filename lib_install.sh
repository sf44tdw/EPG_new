#!/bin/bash

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

echo "***************************************************************************************************************************************" >> ${LOGFILE}
cd LoggerFactory
mvn install >> ${LOGFILE} 2>&1
echo "***************************************************************************************************************************************" >> ${LOGFILE}

echo "" >> ${LOGFILE}
cd ../

echo "***************************************************************************************************************************************" >> ${LOGFILE}
cd TestTool
mvn install >> ${LOGFILE} 2>&1
echo "***************************************************************************************************************************************" >> ${LOGFILE}

echo "" >> ${LOGFILE}
cd ../

echo "***************************************************************************************************************************************" >> ${LOGFILE}
cd ReverseLookUpMapFactory
mvn install >> ${LOGFILE} 2>&1
echo "***************************************************************************************************************************************" >> ${LOGFILE}

echo "" >> ${LOGFILE}
cd ../

# ここまではできた

rm $_lockfile