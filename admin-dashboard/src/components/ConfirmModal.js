import React from 'react';
import { Modal, View, Text, TouchableOpacity, StyleSheet } from 'react-native';
import { useTranslation } from 'react-i18next';

export default function ConfirmModal({ visible, mensagem, userName, onConfirmar, onCancelar }) {
  const { t } = useTranslation();

  const textoDaMensagem = mensagem
    || (userName ? t('confirmModal.desativarComNome', { nome: userName }) : t('confirmModal.desativarGenerico'));

  return (
    <Modal
      transparent
      visible={visible}
      animationType="fade"
      onRequestClose={onCancelar}
    >
      <TouchableOpacity
        style={styles.overlay}
        activeOpacity={1}
        onPress={onCancelar}
      >
        <TouchableOpacity activeOpacity={1} style={styles.card}>
          <Text style={styles.mensagem}>{textoDaMensagem}</Text>

          <View style={styles.botoesRow}>
            <TouchableOpacity
              onPress={onCancelar}
              style={styles.botaoCancelar}
              accessibilityRole="button"
              accessibilityLabel={t('common.cancelar')}
            >
              <Text style={styles.textoCancelar}>{t('common.cancelar')}</Text>
            </TouchableOpacity>

            <TouchableOpacity
              onPress={onConfirmar}
              style={styles.botaoConfirmar}
              accessibilityRole="button"
              accessibilityLabel={t('common.confirmar')}
            >
              <Text style={styles.textoConfirmar}>{t('common.confirmar')}</Text>
            </TouchableOpacity>
          </View>
        </TouchableOpacity>
      </TouchableOpacity>
    </Modal>
  );
}

const styles = StyleSheet.create({
  overlay: {
    flex: 1,
    backgroundColor: 'rgba(0, 0, 0, 0.35)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  card: {
    backgroundColor: '#FFFFFF',
    borderRadius: 16,
    borderWidth: 2,
    borderColor: '#BC86C4',
    paddingVertical: 28,
    paddingHorizontal: 40,
    minWidth: 340,
    maxWidth: 420,
    alignItems: 'center',
    gap: 24,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.15,
    shadowRadius: 8,
    elevation: 8,
  },
  mensagem: {
    fontSize: 17,
    color: '#2F3036',
    textAlign: 'center',
    lineHeight: 24,
  },
  botoesRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    width: '100%',
    paddingHorizontal: 8,
  },
  botaoCancelar: {
    paddingVertical: 8,
    paddingHorizontal: 16,
  },
  textoCancelar: {
    color: '#BC86C4',
    fontSize: 15,
    fontWeight: '500',
  },
  botaoConfirmar: {
    paddingVertical: 8,
    paddingHorizontal: 16,
  },
  textoConfirmar: {
    color: '#2F3036',
    fontSize: 15,
    fontWeight: '700',
  },
});