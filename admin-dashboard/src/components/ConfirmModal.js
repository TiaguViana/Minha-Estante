// Arquivo: src/components/ConfirmModal.js
import React from 'react';
import { Modal, View, Text, TouchableOpacity, StyleSheet } from 'react-native';

/**
 * Modal de confirmação de desativação de usuário.
 *
 * @param {boolean} visible - controla se o modal está visível
 * @param {string} userName - nome do usuário que será desativado (exibido na mensagem)
 * @param {function} onConfirmar - chamada quando o admin clica em "Confirmar"
 * @param {function} onCancelar - chamada quando o admin clica em "Cancelar" ou no overlay
 */
export default function ConfirmModal({ visible, userName, onConfirmar, onCancelar }) {
  return (
    <Modal
      transparent
      visible={visible}
      animationType="fade"
      onRequestClose={onCancelar}
    >
      {/* Overlay escurecido que cobre a tela toda */}
      <TouchableOpacity
        style={styles.overlay}
        activeOpacity={1}
        onPress={onCancelar}
      >
        {/* O card do modal. O TouchableOpacity interno evita que tocar
            dentro do card feche o modal (o toque "para" aqui). */}
        <TouchableOpacity activeOpacity={1} style={styles.card}>
          <Text style={styles.mensagem}>
            Deseja desativar {userName ? `"${userName}"` : 'esse usuário'}?
          </Text>

          <View style={styles.botoesRow}>
            <TouchableOpacity onPress={onCancelar} style={styles.botaoCancelar}>
              <Text style={styles.textoCancelar}>Cancelar</Text>
            </TouchableOpacity>

            <TouchableOpacity onPress={onConfirmar} style={styles.botaoConfirmar}>
              <Text style={styles.textoConfirmar}>Confirmar</Text>
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
    borderColor: '#BC86C4', // mesma cor da variante purple do MetricCard
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
    color: '#BC86C4', // roxo igual ao mockup
    fontSize: 15,
    fontWeight: '500',
  },
  botaoConfirmar: {
    paddingVertical: 8,
    paddingHorizontal: 16,
  },
  textoConfirmar: {
    color: '#2F3036', // preto/escuro igual ao mockup
    fontSize: 15,
    fontWeight: '700',
  },
});